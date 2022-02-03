package com.example.f1racingcompanion.data.positiondatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PositionEntryDto(
    @Json(name = "Timestamp")
    val time: String,
    @Json(name = "Entries")
    val cars: Map<Int, CarPositionDto>
)
