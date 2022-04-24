package com.example.f1racingcompanion.data.timingappdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DriverTimingDto(
    @Json(name = "Stints")
    val stints: Map<Int, Stint>,
    @Json(name = "Line")
    val position: Int?,
)
