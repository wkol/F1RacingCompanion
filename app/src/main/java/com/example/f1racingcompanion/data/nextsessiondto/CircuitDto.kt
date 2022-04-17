package com.example.f1racingcompanion.data.nextsessiondto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CircuitDto(
    @Json(name = "circuitName")
    val circuitName: String,
    @Json(name = "circuitId")
    val circuitId: String
)
