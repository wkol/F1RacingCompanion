package com.example.f1racingcompanion.data.webSocketResponse

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("H")
    val hubName: String,
    @SerializedName("M")
    val messageDescription: String,
    @SerializedName("A")
    val messageData: List<String>
)
