package com.example.f1racingcompanion.data.positiondatadto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarPositionDto(
    val status: String,
    val xPosition: Int,
    val yPosition: Int,
    val zPosition: Int
)
