package com.example.f1racingcompanion.data.nextsessiondto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class NextSessionDto(
    @Json(name = "state")
    val state: String,
    @Json(name = "gmtOffset")
    val gmtOffset: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "startTime")
    val startTime: LocalDateTime
)
