package com.example.f1racingcompanion.data.liveTimingData

import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import com.example.f1racingcompanion.utils.Constants
import java.time.LocalDateTime

sealed class LiveTimingData(val type: Constants.LiveTimingDataType?, val date: LocalDateTime?) {
    class LiveTimingDataDto(val data: TimingDataDto?, date: LocalDateTime?) :
        LiveTimingData(Constants.LiveTimingDataType.TIMING_DATA, date)

    class LiveTimingAppDataDto(val data: TimingAppDataDto?, date: LocalDateTime?) :
        LiveTimingData(Constants.LiveTimingDataType.TIMING_APP_DATA, date)

    class LiveCarDataDto(val data: CarDataDto?, date: LocalDateTime?) :
        LiveTimingData(Constants.LiveTimingDataType.CAR_DATA, date)

    class LiveTimingStatsDto(val data: TimingStatsDto?, date: LocalDateTime?) :
        LiveTimingData(Constants.LiveTimingDataType.TIMING_STATS, date)

    class LivePositionDataDto(val data: PositionDataDto?, date: LocalDateTime?) :
        LiveTimingData(Constants.LiveTimingDataType.POSITION_DATA, date)

    class LiveTimingBulkMessage(val data: String, type: Constants.LiveTimingDataType, date: LocalDateTime?) :
        LiveTimingData(type, date)
}
