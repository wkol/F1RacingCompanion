package com.example.f1racingcompanion.data.timingappdatadto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimingAppDataDto(
    val time: String,
    val lapInfo: Map<Int, DriverTimingDto>
)
