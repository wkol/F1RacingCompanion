package com.example.f1racingcompanion.data.webSocketResponse


import com.google.gson.annotations.SerializedName

data class WebSocketResponse(
    @SerializedName("C")
    val c: String,
    @SerializedName("M")
    val m: List<Message>,
    @SerializedName("S")
    val s: Int
)