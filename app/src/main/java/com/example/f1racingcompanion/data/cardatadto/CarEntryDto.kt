package com.example.f1racingcompanion.data.cardatadto

import com.google.gson.annotations.SerializedName

data class CarEntryDto(
    @SerializedName("Utc")
    val time: String,
    @SerializedName("Cars")
    val cars: List<CarDto>
)