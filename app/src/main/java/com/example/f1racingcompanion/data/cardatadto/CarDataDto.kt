package com.example.f1racingcompanion.data.cardatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarDataDto(
    @Json(name = "Entries")
    val entries: List<Map<Int, CarEntryDto>>
)
