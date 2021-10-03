package com.example.f1racingcompanion.data.positiondatadto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PositionDataDto(
    val entries: List<PositionEntryDto>
    )
