package com.example.f1racingcompanion.data.liveTimingData

import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import java.time.LocalDateTime

sealed class LiveTimingData<T>(val name: String?, val data: T?, val date: LocalDateTime?) {
    class LiveTimingDataDto(data: TimingDataDto?, date: LocalDateTime?) : LiveTimingData<TimingDataDto>("TimingData", data, date)
    class LiveTimingAppDataDto(data: TimingAppDataDto?, date: LocalDateTime?) : LiveTimingData<TimingAppDataDto>("TimingAppData", data, date)
    class LiveCarDataDto(data: CarDataDto?, date: LocalDateTime?) : LiveTimingData<CarDataDto>("CarData.z", data, date)
    class LiveTimingStatsDto(data: TimingStatsDto?, date: LocalDateTime?) : LiveTimingData<TimingStatsDto>("TimingStats", data, date)
    class LivePositionDataDto(data: PositionDataDto?, date: LocalDateTime?) : LiveTimingData<PositionDataDto>("Position.z", data, date)
}
