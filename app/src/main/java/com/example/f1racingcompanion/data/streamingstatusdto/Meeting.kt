package com.example.f1racingcompanion.data.streamingstatusdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meeting(
    @Json(name = "Circuit")
    val circuit: Circuit,
    @Json(name = "Country")
    val country: Country,
    @Json(name = "Key")
    val key: Int,
    @Json(name = "Location")
    val location: String,
    @Json(name = "Name")
    val name: String
)
