package com.example.f1racingcompanion.timing

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.data.Formula1Repository
import com.example.f1racingcompanion.data.LiveTimingRepository
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.NegotiateCookieJar
import com.example.f1racingcompanion.utils.toListTimingAppData
import com.example.f1racingcompanion.utils.toListTimingData
import com.example.f1racingcompanion.utils.toPositionDataList
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TimingViewModel @Inject constructor(
    formula1Repository: Formula1Repository,
    negotiateCookieJar: NegotiateCookieJar,
    moshi: Moshi
) : ViewModel() {

    private val _standing: MutableMap<Int, F1DriverListElement> = mutableMapOf()
    val standing: StateFlow<List<F1DriverListElement>>
        get() = MutableStateFlow(_standing.values.toList())
    private var _fastestLap: MutableStateFlow<FastestRaceLap> = MutableStateFlow(FastestRaceLap(0, "-"))
    val fastestLap: StateFlow<FastestRaceLap>
        get() = _fastestLap
    private lateinit var liveTimingRepository: LiveTimingRepository
    private val _driversPositions = MutableStateFlow<MutableMap<Int, Offset>>(mutableMapOf())
    val driversPosition: StateFlow<Map<Int, Offset>>
        get() = _driversPositions
    val offsets = Constants.OFFSETMAP["russia"]!!

    init {
        viewModelScope.launch {
            val token = formula1Repository.getConnectionToken().take(2).last()
            val service = LiveTimingService.create(
                token.data!!,
                negotiateCookieJar.getCookies().first(),
                moshi
            )
            liveTimingRepository = LiveTimingRepository(service)
            liveTimingRepository.subscribe()
            startUpdatingData()
        }
    }

    private suspend fun startUpdatingData() {
        merge(liveTimingRepository.getTimingData(), liveTimingRepository.getTimingAppData(), liveTimingRepository.getPositions()).collect {
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
            _standing[element.driverNum]?.let {
                it.lastLapTime = element.lapTime ?: it.lastLapTime
                it.startingGridPos = element.startingGridPos ?: it.startingGridPos
                it.tires = element.currentTires ?: it.tires
                _standing[element.driverNum] = it.copy()
            }
        }
    }

    private fun updatePositionData(data: PositionDataDto) {
        val parsedData = data.toPositionDataList()
        for (timestamp in parsedData) {
            for (driver in timestamp.position) {
                _driversPositions.value[driver.driverNum] = Offset(driver.xPos - offsets.xOffset, offsets.yOffset + driver.yPos)
            }
        }
    }

    private fun updateTimingData(data: TimingDataDto) {
        val parsedData = data.toListTimingData()
        for (element in parsedData) {
            _standing[element.driverNum]?.let {
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
                        _fastestLap.value = FastestRaceLap(element.driverNum, element.fastestLap.time!!)
                    }
                }
                _standing[element.driverNum] = it.copy()
            }
        }
    }
}
