package com.example.f1racingcompanion.data.positiondatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarPositionDto(
    @Json(name = "Status")
    val status: String,
    @Json(name = "X")
    val xPosition: Int,
    @Json(name = "Y")
    val yPosition: Int,
    @Json(name = "Z")
    val zPosition: Int
)
