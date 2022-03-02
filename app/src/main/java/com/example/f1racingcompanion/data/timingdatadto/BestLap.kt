package com.example.f1racingcompanion.data.timingdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BestLap(
    @Json(name = "Value")
    val time: String?,
    @Json(name = "Lap")
    val lapNum: Int?
)
