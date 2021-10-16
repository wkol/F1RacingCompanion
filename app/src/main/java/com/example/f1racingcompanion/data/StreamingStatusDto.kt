package com.example.f1racingcompanion.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamingStatusDto(
    @Json(name = "Status")
    val status: String
)
