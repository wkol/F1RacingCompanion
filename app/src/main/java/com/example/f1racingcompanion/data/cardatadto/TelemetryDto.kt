package com.example.f1racingcompanion.data.cardatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TelemetryDto(
    @Json(name = "0")
    val rpmValue: Int,
    @Json(name = "2")
    val speed: Int,
    @Json(name = "3")
    val currentGear: Byte,
    @Json(name = "4")
    val throttleValue: Int,
    @Json(name = "5")
    val brakeValue: Int,
    @Json(name = "45")
    val DRSValue: Int
)
