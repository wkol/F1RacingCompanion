package com.example.f1racingcompanion.data.streamingstatusdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Country(
    @Json(name="Code")
    val code: String,
    @Json(name="Key")
    val key: Int,
    @Json(name="Name")
    val name: String
)