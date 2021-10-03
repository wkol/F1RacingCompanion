package com.example.f1racingcompanion.data

import com.squareup.moshi.Json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subscribe(
    @Json(name = "H")
    val hub: String,
    @Json(name = "M")
    val method: String,
    @Json(name = "A")
    val arguments: List<List<String>>,
    @Json(name = "I")
    val increment: Int
)
