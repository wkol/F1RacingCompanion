package com.example.f1racingcompanion.data.timingappdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimingAppDataDto(
    @Json(name = "Lines")
    val lapInfo: Map<Int, DriverTimingDto>
)
