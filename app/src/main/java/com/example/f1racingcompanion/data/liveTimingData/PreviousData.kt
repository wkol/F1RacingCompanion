package com.example.f1racingcompanion.data.liveTimingData

import com.example.f1racingcompanion.data.liveTimingData.previousdata.DriverInfoDto
import com.example.f1racingcompanion.data.liveTimingData.previousdata.PreviousTimingAppDataDto
import com.example.f1racingcompanion.data.liveTimingData.previousdata.PreviousTimingDataDto
import com.example.f1racingcompanion.utils.Constants
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class PreviousData(
    val timingAppDataDto: PreviousTimingAppDataDto? = null,
    val timingDataDto: PreviousTimingDataDto? = null,
    val drivers: Map<Int, DriverInfoDto>,
) : LiveTimingData(Constants.LiveTimingDataType.PREVIOUS_DATA, null)
