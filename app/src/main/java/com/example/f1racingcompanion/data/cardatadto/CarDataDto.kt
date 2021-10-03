package com.example.f1racingcompanion.data.cardatadto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarDataDto(
    val entries: List<CarEntryDto>)

