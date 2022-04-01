package com.example.f1racingcompanion.data.previousdata

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PreviousTimingAppDataDto(
    @Json(name = "Lines")
    val lapInfo: Map<Int, PreviousDriverTimingDto>
)
