package com.example.f1racingcompanion.di.data


import com.google.gson.annotations.SerializedName

data class StreamingStatusDto(
    @SerializedName("Status")
    val status: String
)