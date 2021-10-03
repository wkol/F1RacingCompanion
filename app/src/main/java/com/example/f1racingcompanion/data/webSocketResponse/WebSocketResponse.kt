package com.example.f1racingcompanion.data.webSocketResponse


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WebSocketResponse(
    @Json(name = "C")
    val c: String,
    @Json(name = "M")
    val m: List<Message>,
    @Json(name = "S")
    val s: Int
)