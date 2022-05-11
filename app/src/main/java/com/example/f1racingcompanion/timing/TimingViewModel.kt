package com.example.f1racingcompanion.timing

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
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.NegotiateCookieJar
import com.example.f1racingcompanion.utils.toF1DriverListElementList
import com.example.f1racingcompanion.utils.toListTimingAppData
import com.example.f1racingcompanion.utils.toListTimingData
import com.example.f1racingcompanion.utils.toPositionDataList
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class TimingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    liveTimingFormula1Repository: LiveTimingFormula1Repository,
    negotiateCookieJar: NegotiateCookieJar,
    moshi: Moshi,
) : ViewModel() {

    private var _standing: SnapshotStateMap<Int, F1DriverListElement> = mutableStateMapOf()

    private var liveTimingRepository: LiveTimingRepository

    val standing: StateFlow<List<F1DriverListElement>>
        get() = MutableStateFlow(_standing.values.toList().sortedBy { it.position }).asStateFlow()

    private val _fastestLap: MutableStateFlow<FastestRaceLap> =
        MutableStateFlow(FastestRaceLap(0, "-"))
    val fastestLap: StateFlow<FastestRaceLap>
        get() = _fastestLap

    private val _driversPositions: SnapshotStateMap<Int, Position> = mutableStateMapOf()
    val driversPosition: StateFlow<List<Position>>
        get() = MutableStateFlow(_driversPositions.values.toList()).asStateFlow()

    val circuitInfo = Constants.CIRCUITS[savedStateHandle.get<String>("circuit_id")]
        ?: CircuitInfo.getUnknownCircuitInfo()

    val sessionType = savedStateHandle.get<String>("session_type")!!

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    init {
        runBlocking {
            val token = liveTimingFormula1Repository.getConnectionToken().take(2).toList()[1]
            val service = LiveTimingService.create(
                token.data!!,
                negotiateCookieJar.getCookies().first(),
                moshi
            )
            liveTimingRepository = LiveTimingRepository(service)
            liveTimingRepository.startWebSocket().launchIn(viewModelScope)
        }
        refreshWebSocket()
    }

    fun refreshWebSocket() {
        if (!isLoading.value || !liveTimingRepository.isOpen) return
        viewModelScope.launch {
            _isLoading.value = true
            syncData()
            startUpdatingData()
            _isLoading.value = false
        }
    }

    private suspend fun syncData() {
        liveTimingRepository.subscribe()
        val previousData = liveTimingRepository.getPreviousData().take(1).first()
        _standing =
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

    private fun updateStandings(newData: LiveTimingData<*>) {
        when (newData) {
            is LiveTimingData.LiveTimingAppDataDto -> updateTimingAppData(newData.data as TimingAppDataDto)
            is LiveTimingData.LiveTimingDataDto -> updateTimingData(newData.data as TimingDataDto)
            is LiveTimingData.LivePositionDataDto -> updatePositionData(newData.data as PositionDataDto)
            else -> {}
        }
    }

    private fun updateTimingAppData(data: TimingAppDataDto) {
        val parsedData = data.toListTimingAppData()
        for (element in parsedData) {
            _standing[element.driverNum]?.let {
                _standing[element.driverNum] = it.copy(
                    lastLapTime = element.lapTime ?: it.lastLapTime,
                    tires = if (element.currentTires?.isNew != null) element.currentTires else it.tires
                )
            }
        }
    }

    private fun updatePositionData(data: PositionDataDto) {
        val parsedData = data.toPositionDataList()
        for (timestamp in parsedData) {
            for (driver in timestamp.position) {
                _driversPositions[driver.driverNum] =
                    Position(
                        _standing[driver.driverNum]?.teamColor ?: 0,
                        Offset(
                            (circuitInfo.circuitOffset.xOffset - driver.xPos).absoluteValue,
                            (circuitInfo.circuitOffset.yOffset - driver.yPos).absoluteValue
                        )
                    )
            }
        }
    }

    private fun updateTimingData(data: TimingDataDto) {
        val parsedData = data.toListTimingData()
        for (element in parsedData) {
            _standing[element.driverNum]?.let {
                if (element.overallFastest == true && element.fastestLap?.time != null) {
                    _fastestLap.value = FastestRaceLap(element.driverNum, element.fastestLap.time)
                }
                _standing[element.driverNum] = it.copy(
                    interval = element.gapToNext ?: it.interval,
                    toFirst = element.gapToLeader ?: it.toFirst,
                    position = element.position ?: it.position,
                    retired = element.retired ?: element.knockedOut ?: it.retired,
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
    }
}
