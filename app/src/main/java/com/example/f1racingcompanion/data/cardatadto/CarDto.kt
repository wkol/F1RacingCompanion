package com.example.f1racingcompanion.data.cardatadto

import com.squareup.moshi.Json

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class CarDto(
    val driverNum: String,
    @Json(name = "Channels")
    val telemetry: TelemetryDto
)
