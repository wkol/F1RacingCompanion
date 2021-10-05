package com.example.f1racingcompanion.data.Message

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    @Json(name = "H")
    val hubName: String,
    @Json(name = "M")
    val messageDescription: String,
    @Json(name = "A")
    val messageData: List<String>
)
