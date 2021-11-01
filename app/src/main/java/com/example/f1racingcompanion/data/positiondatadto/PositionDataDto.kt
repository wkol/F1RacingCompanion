package com.example.f1racingcompanion.data.positiondatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PositionDataDto(
    @Json(name = "Position")
    val entries: List<PositionEntryDto>
)
