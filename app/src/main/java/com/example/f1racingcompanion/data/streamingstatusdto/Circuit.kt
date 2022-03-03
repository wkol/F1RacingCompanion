package com.example.f1racingcompanion.data.streamingstatusdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Circuit(
    @Json(name = "Key")
    val key: Int,
    @Json(name = "ShortName")
    val shortName: String
)
