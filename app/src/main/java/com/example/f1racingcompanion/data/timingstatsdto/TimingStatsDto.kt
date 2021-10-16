package com.example.f1racingcompanion.data.timingstatsdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimingStatsDto(
    @Json(name = "Lines")
    val lines: Map<String, TimingStat>
)