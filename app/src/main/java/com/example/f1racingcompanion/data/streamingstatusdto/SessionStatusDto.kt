package com.example.f1racingcompanion.data.streamingstatusdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionStatusDto(
    @Json(name = "Status")
    val status: String?
)
