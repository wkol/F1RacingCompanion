package com.example.f1racingcompanion.data.timingdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SectorValue(
    @Json(name = "Value")
    val value: Int?,
    @Json(name = "PersonalFastest")
    val personalFastest: Boolean?,
    @Json(name = "OverallFastest")
    val overallFastest: Boolean?
)