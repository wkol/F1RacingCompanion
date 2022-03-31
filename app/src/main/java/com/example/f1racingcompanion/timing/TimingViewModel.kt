package com.example.f1racingcompanion.timing

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.data.Formula1Repository
import com.example.f1racingcompanion.data.LiveTimingProxyRepository
import com.example.f1racingcompanion.data.LiveTimingRepository
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.NegotiateCookieJar
import com.example.f1racingcompanion.utils.Result
import com.example.f1racingcompanion.utils.toF1DriverElement
import com.example.f1racingcompanion.utils.toListTimingAppData
import com.example.f1racingcompanion.utils.toListTimingData
import com.example.f1racingcompanion.utils.toPositionDataList
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

typealias Position = Pair<Long, Offset> // Temporary solution to store colors with driver position on track

@ExperimentalCoroutinesApi
@HiltViewModel
class TimingViewModel @Inject constructor(
    private val proxyRepository: LiveTimingProxyRepository,
    formula1Repository: Formula1Repository,
    negotiateCookieJar: NegotiateCookieJar,
    moshi: Moshi
) : ViewModel() {

    private var _standing: MutableStateFlow<MutableMap<Int, F1DriverListElement>> = MutableStateFlow(
        mutableMapOf()
    )

    private var liveTimingRepository: LiveTimingRepository

    val standing: StateFlow<Map<Int, F1DriverListElement>>
        get() = _standing

    private val _fastestLap: MutableStateFlow<FastestRaceLap> =
        MutableStateFlow(FastestRaceLap(0, "-"))
    val fastestLap: StateFlow<FastestRaceLap>
        get() = _fastestLap

    private val _driversPositions = MutableStateFlow<MutableMap<Int, Position>>(mutableMapOf())
    val driversPosition: StateFlow<Map<Int, Position>>
        get() = _driversPositions

    val circuitInfo = CircuitInfo("bahrain", Constants.OFFSETMAP["bahrain"]!!, "Bahrain")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    init {
        runBlocking {
            val token = formula1Repository.getConnectionToken().take(2).toList()[1]
            val service = LiveTimingService.create(
                token.data!!,
                negotiateCookieJar.getCookies().first(),
                moshi
            )
            liveTimingRepository = LiveTimingRepository(service)
        }
    }

    suspend fun openWebSocket() {
        liveTimingRepository.subscribe()
        startUpdatingData()
    }

    suspend fun syncData() {
        proxyRepository.getSyncedData().collect { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _standing.value = result.data!!.associateBy({ it.carNumber }, { it.toF1DriverElement() }).toMutableMap()
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _standing.value = mutableMapOf()
                    _isLoading.value = false
                }
            }
        }
    }

    private suspend fun startUpdatingData() {
        merge(
            liveTimingRepository.getTimingData(),
            liveTimingRepository.getTimingAppData(),
            liveTimingRepository.getPositions()
        ).collect {
            updateStandings(it)
        }
    }

    private fun updateStandings(newData: LiveTimingData<*>) {
        when (newData.name) {
            "TimingAppData" -> updateTimingAppData(newData.data as TimingAppDataDto)
            "TimingData" -> updateTimingData(newData.data as TimingDataDto)
            "Position.z" -> updatePositionData(newData.data as PositionDataDto)
        }
    }

    private fun updateTimingAppData(data: TimingAppDataDto) {
        val parsedData = data.toListTimingAppData()
        for (element in parsedData) {
            _standing.value[element.driverNum]?.let {
                it.lastLapTime = element.lapTime ?: it.lastLapTime
                it.startingGridPos = element.startingGridPos ?: it.startingGridPos
                it.tires = element.currentTires ?: it.tires
                _standing.value[element.driverNum] = it.copy()
            }
        }
    }

    private fun updatePositionData(data: PositionDataDto) {
        val parsedData = data.toPositionDataList()
        for (timestamp in parsedData) {
            for (driver in timestamp.position) {
                _driversPositions.value[driver.driverNum] =
                    Position(
                        _standing.value[driver.driverNum]?.teamColor ?: 0,
                        Offset(driver.xPos - circuitInfo.circuitOffset.xOffset, -circuitInfo.circuitOffset.yOffset + driver.yPos)
                    )
            }
        }
    }

    private fun updateTimingData(data: TimingDataDto) {
        val parsedData = data.toListTimingData()
        for (element in parsedData) {
            _standing.value[element.driverNum]?.let {
                it.interval = element.gapToNext ?: it.interval
                it.toFirst = element.gapToLeader ?: it.toFirst
                it.position = element.position ?: it.position
                it.retired = element.retired ?: it.retired
                it.inPit = element.inPit ?: it.inPit
                it.pitstopCount = element.pits ?: it.pitstopCount
                if (element.sector != null) {
                    it.updateLastSectors(element.sector)
                }
                if (element.fastestLap != null) {
                    it.bestLap = element.fastestLap
                    if (element.overallFastest == true) {
                        _fastestLap.value =
                            FastestRaceLap(element.driverNum, element.fastestLap.time!!)
                    }
                }
                _standing.value[element.driverNum] = it.copy()
            }
        }
    }
}
