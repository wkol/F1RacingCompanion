package com.example.f1racingcompanion.ui.timings

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.f1racingcompanion.model.F1Driver
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.utils.*
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TimingViewModel @Inject constructor(
    formula1Repository: Formula1Repository,
    negotiateCookieJar: NegotiateCookieJar,
    moshi: Moshi
) : ViewModel() {

    private val _standing: MutableMap<Int, F1DriverListElement> = mutableMapOf()
    val standing: SnapshotStateList<F1DriverListElement>
    get() = _standing.values.toMutableStateList()
    private var bestLap by mutableStateOf("")
    private lateinit var liveTimingRepository: LiveTimingRepository
    var drivers = mutableStateMapOf<F1Driver, Offset>(
        F1Driver.getDriverByNumber(33) to Offset(
            -12055F - Constants.OFFSETMAP["russia"]!![0],
            Constants.OFFSETMAP["russia"]!![3] + 1960.0F
        )
    )
        private set

    init {
        viewModelScope.launch {
            val token = formula1Repository.getConnectionToken().take(2).last()
            val service = LiveTimingService.create(
                token.data!!,
                negotiateCookieJar.loadForRequest(
                    HttpUrl.Builder().scheme("http").host("google.com").build()
                ).first(),
                moshi
            )
            liveTimingRepository = LiveTimingRepository(service)
            liveTimingRepository.subscribe()
            liveTimingRepository.startWebSocket().launchIn(viewModelScope)
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
            _standing[element.driver.carNumber]?.let {
                it.lapTime = element.lapTime ?: it.lapTime
                if (element.currentTires?.isNew == true) {
                    it.tires = element.currentTires
                }
            }
        }
    }

    private fun updatePositionData(data: PositionDataDto) {
        val parsedData = data.toPositionDataList()
        for (timestamp in parsedData) {
            for (driver in timestamp.position) {
                drivers[driver.driver] = Offset(driver.xPos, driver.yPos)
            }
        }
    }

    private fun updateTimingData(data: TimingDataDto) {
        val parsedData = data.toListTimingData()
        for (element in parsedData) {
            _standing[element.driver.carNumber]?.let {
                it.interval = element.gapToNext ?: it.interval
                it.toFirst = element.gapToLeader ?: it.toFirst
                it.position = element.position ?: it.position
                if (element.sector != null) {
                    it.updateLastSectors(element.sector)
                }
                if (element.fastestLap == true) {
                    bestLap = element.lastLapTime!!
                }
            }
        }
    }
}