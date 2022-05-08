package com.example.f1racingcompanion.data.previousdata

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PreviousTimingDataDto(
    @Json(name = "Lines")
    val lines: Map<Int, PreviousTiming>,
    @Json(name = "SessionPart")
    val sessionPart: Int?,
)
