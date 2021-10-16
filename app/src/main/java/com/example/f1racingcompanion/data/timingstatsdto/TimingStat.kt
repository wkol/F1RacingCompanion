package com.example.f1racingcompanion.data.timingstatsdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimingStat(
    @Json(name = "BestSectors")
    val bestSectors: Map<String, BestStat>?,
    @Json(name = "BestSpeeds")
    val bestSpeeds: Map<String, BestStat>?,
    @Json(name = "PersonalBestLapTime")
    val personalBestLapTime: BestStat?

)
