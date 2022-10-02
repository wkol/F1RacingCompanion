package com.example.f1racingcompanion.timing

import android.app.Application
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.data.LiveTimingFormula1Repository
import com.example.f1racingcompanion.data.LiveTimingRepository
import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.TelemetryInfo
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.NegotiateCookieJar
import com.example.f1racingcompanion.utils.toF1DriverListElementList
import com.example.f1racingcompanion.utils.toListTimingAppData
import com.example.f1racingcompanion.utils.toListTimingData
import com.example.f1racingcompanion.utils.toPositionDataList
import com.example.f1racingcompanion.utils.toTelemtryInfoList
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class TimingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    liveTimingFormula1Repository: LiveTimingFormula1Repository,
    negotiateCookieJar: NegotiateCookieJar,
    moshi: Moshi,
    application: Application
) : ViewModel() {

    private var _standing: SnapshotStateMap<Int, F1DriverListElement> = mutableStateMapOf()

    private lateinit var liveTimingRepository: LiveTimingRepository

    val standing: StateFlow<List<F1DriverListElement>>
        get() = MutableStateFlow(_standing.values.toList().sortedBy { it.position }).asStateFlow()

    private val _fastestLap: MutableStateFlow<FastestRaceLap> =
        MutableStateFlow(FastestRaceLap(0, "-"))
    val fastestLap: StateFlow<FastestRaceLap>
        get() = _fastestLap

    private val _driversPositions: MutableStateFlow<List<Position>> = MutableStateFlow(emptyList())
    val driversPosition: StateFlow<List<Position>>
        get() = _driversPositions

    val circuitInfo = Constants.CIRCUITS[savedStateHandle.get<String>("circuit_id")]
        ?: CircuitInfo.getUnknownCircuitInfo()

    private val _sessionType = MutableStateFlow(savedStateHandle.get<String>("session_type")!!)
    val sessionType = _sessionType

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private var driverTelemetryNum: Int? = null

    private val _driverTelemetry =
        MutableStateFlow(
            TelemetryInfo(
                delayInMilis = 0,
                currentSpeed = 0,
                currentRPMValue = 0,
                isDRSEnabled = false,
                currentThrottleValue = 0,
                currentBrakeValue = 0,
                currentGear = 0,
                driverStr = null,
                sectors = emptyMap()
            )
        )
    val driverTelemetry: StateFlow<TelemetryInfo>
        get() = _driverTelemetry

    private var telemetryJob: Job? = null

    init {
        viewModelScope.launch {
            val token = liveTimingFormula1Repository.getConnectionToken().take(2).toList()[1]
            val service = LiveTimingService.create(
                token.data!!,
                negotiateCookieJar.getCookies().first(),
                moshi,
                application
            )
            liveTimingRepository = LiveTimingRepository(service)
            liveTimingRepository.startWebSocket().launchIn(viewModelScope)
            syncData()
            startUpdatingData()
            _isLoading.update { false }
        }
    }

    fun refreshWebSocket() {
        if (isLoading.value || liveTimingRepository.isOpen) return
        viewModelScope.launch {
            _isLoading.update { true }
            syncData()
            startUpdatingData()
            _isLoading.update { false }
        }
    }

    private suspend fun syncData() {
        val previousData = liveTimingRepository.getPreviousData().first()
        previousData.timingDataDto?.sessionPart?.let { _sessionType.value = "Q$it" }
        _standing +=
            previousData.toF1DriverListElementList().map { it.carNumber to it }.toMutableStateMap()
    }

    private fun startUpdatingData() {
        merge(
            liveTimingRepository.getTimingData(),
            liveTimingRepository.getTimingAppData(),
            liveTimingRepository.getPositions()
        ).onEach {
            updateStandings(it)
        }.launchIn(viewModelScope)
    }

    private fun updateStandings(newData: LiveTimingData) {
        when (newData) {
            is LiveTimingData.LiveTimingAppDataDto -> updateTimingAppData(newData.data as TimingAppDataDto)
            is LiveTimingData.LiveTimingDataDto -> updateTimingData(newData.data as TimingDataDto)
            is LiveTimingData.LivePositionDataDto -> updatePositionData(newData.data as PositionDataDto)
            else -> {}
        }
    }

    private fun updateCarData(carDataDto: CarDataDto) {
        driverTelemetryNum?.let {
            val sectors = _standing[it]?.lastSectors ?: emptyMap()
            val name = _standing[it]?.shortcut ?: "-"
            val parsedData = carDataDto.toTelemtryInfoList(it, name, sectors)
            viewModelScope.launch {
                for (timestamp in parsedData) {
                    _driverTelemetry.value = timestamp
                    delay(timestamp.delayInMilis)
                }
            }
        }
    }

    private fun updateTimingAppData(data: TimingAppDataDto) {
        val parsedData = data.toListTimingAppData()
        for (element in parsedData) {
            _standing[element.driverNum]?.let {
                _standing[element.driverNum] = it.copy(
                    lastLapTime = element.lapTime ?: it.lastLapTime,
                    tires = if (element.currentTires?.isNew != null) element.currentTires else it.tires.copy(
                        tyreAge = element.currentTires?.tyreAge ?: it.tires.tyreAge
                    ),
                )
            }
        }
    }

    private fun updatePositionData(data: PositionDataDto) {
        val parsedData = data.toPositionDataList()
        viewModelScope.launch {
            for (timestamp in parsedData) {
                delay(timestamp.delayInMilis)
                _driversPositions.update {
                    timestamp.position.map { driver ->
                        Position(
                            _standing[driver.driverNum]?.teamColor ?: 0,
                            Offset(
                                (circuitInfo.circuitOffset.xOffset - driver.xPos).absoluteValue,
                                (circuitInfo.circuitOffset.yOffset - driver.yPos).absoluteValue
                            ),
                            driverTelemetryNum == driver.driverNum
                        )
                    }
                }
            }
        }
    }

    private fun updateTimingData(data: TimingDataDto) {
        val parsedData = data.toListTimingData()
        for (element in parsedData) {
            _standing[element.driverNum]?.let {
                if (element.overallFastest == true && element.fastestLap?.time != null) {
                    _fastestLap.update {
                        FastestRaceLap(
                            element.driverNum,
                            element.fastestLap.time
                        )
                    }
                }
                _standing[element.driverNum] = it.copy(
                    interval = element.gapToNext ?: it.interval,
                    toFirst = element.gapToLeader ?: it.toFirst,
                    position = element.position ?: it.position,
                    retired = element.knockedOut ?: element.retired ?: it.retired,
                    inPit = element.inPit ?: it.inPit,
                    pitstopCount = element.pits ?: it.pitstopCount,
                    lastSectors = when {
                        element.sector.isNullOrEmpty() -> it.lastSectors
                        element.sector["0"]?.value.isNullOrEmpty() -> it.lastSectors.plus(element.sector.filter { sector -> !(sector.value.value.isNullOrEmpty()) })
                        else -> element.sector
                    },
                    bestLap = element.fastestLap ?: it.bestLap,
                )
            }
        }
        if (data.sessionPart != null)
            _sessionType.update { "Q${data.sessionPart}" }
    }

    fun startDriverTelemetry(driverNum: Int) {
        stopDriverTelemetry()
        Timber.d("startDriverTelemetry $driverNum")
        driverTelemetryNum = driverNum
        _driverTelemetry.value = TelemetryInfo(
            delayInMilis = 0,
            currentSpeed = 0,
            currentRPMValue = 0,
            isDRSEnabled = false,
            currentThrottleValue = 0,
            currentBrakeValue = 0,
            currentGear = 0,
            sectors = _standing[driverNum]?.lastSectors ?: emptyMap(),
            driverStr = _standing[driverNum]?.shortcut ?: "-"
        )
        telemetryJob = liveTimingRepository.getDriverTelemetry().cancellable()
            .onEach { updateCarData(it.data as CarDataDto) }
            .launchIn(viewModelScope)
    }

    fun stopDriverTelemetry() {
        Timber.d("stopDriverTelemetry $driverTelemetryNum")
        telemetryJob?.cancel()
        driverTelemetryNum = null
    }
}
