package com.example.f1racingcompanion.data

import com.google.gson.annotations.SerializedName

data class Subscribe(
    @SerializedName("H")
    val hub: String,
    @SerializedName("M")
    val method: String,
    @SerializedName("A")
    val arguments: List<List<String>>,
    @SerializedName("I")
    val increment: Int
)
