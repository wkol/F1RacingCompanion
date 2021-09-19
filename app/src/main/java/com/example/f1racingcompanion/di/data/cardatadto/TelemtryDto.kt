package com.example.f1racingcompanion.di.data.cardatadto

import com.google.gson.annotations.SerializedName

data class TelemetryDto (
    @SerializedName("0")
    val rmpValue: Int,
    @SerializedName("2")
    val speed: Int,
    @SerializedName("3")
    val currentGear: Byte,
    @SerializedName("4")
    val throttleValue: Int,
    @SerializedName("5")
    val brakeValue: Int,
    @SerializedName("45")
    val DRSValue: Int
)
