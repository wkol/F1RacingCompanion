package com.example.f1racingcompanion.data.timingstatsdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BestStat (
    @Json(name = "Position")
    val position: Int?,
    @Json(name = "Value")
    val value: Int?,
    @Json(name = "Lap")
    val lap: Int?
)