package com.example.f1racingcompanion.data.cardatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarEntryDto(
    @Json(name = "Utc")
    val time: String,
    @Json(name = "Cars")
    val cars: Map<Int, CarDto>
)
