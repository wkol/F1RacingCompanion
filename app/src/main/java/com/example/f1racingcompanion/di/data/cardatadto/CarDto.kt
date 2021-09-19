package com.example.f1racingcompanion.di.data.cardatadto

import com.google.gson.annotations.SerializedName

data class CarDto(
    val driverNum: String,
    @SerializedName("Channels")
    val telemetry: TelemetryDto
)
