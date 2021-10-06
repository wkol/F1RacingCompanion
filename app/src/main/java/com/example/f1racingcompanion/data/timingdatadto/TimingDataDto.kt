package com.example.f1racingcompanion.data.timingdatadto

import com.squareup.moshi.Json

data class TimingDataDto(
    @Json(name = "Lines")
    val lines: Map<String, Timing>
)
