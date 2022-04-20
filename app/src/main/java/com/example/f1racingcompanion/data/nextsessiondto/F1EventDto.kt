package com.example.f1racingcompanion.data.nextsessiondto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class F1EventDto(
    @Json(name = "date")
    val date: String,
    @Json(name = "time")
    val time: String
)
