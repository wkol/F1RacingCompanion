package com.example.f1racingcompanion.data.positiondatadto

import com.google.gson.annotations.SerializedName

data class PositionEntryDto(
    @SerializedName("Timestamp")
    val time: String,
    @SerializedName("Entries")
    val cars: Map<String, CarPositionDto>
)
