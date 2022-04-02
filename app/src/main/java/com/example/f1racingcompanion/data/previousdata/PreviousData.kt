package com.example.f1racingcompanion.data.previousdata

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class PreviousData(
    val timingAppDataDto: PreviousTimingAppDataDto? = null,
    val timingDataDto: PreviousTimingDataDto? = null,
    val drivers: Map<Int, DriverInfoDto>,
)
