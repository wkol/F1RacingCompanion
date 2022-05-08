package com.example.f1racingcompanion.data.timingdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimingDataDto(
    @Json(name = "Lines")
    val lines: Map<Int, Timing>,
    @Json(name = "SessionPart")
    val sessionPart: Int?
)
