package com.example.f1racingcompanion.data.timingdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SectorValue(
    @Json(name = "Value")
    val value: String?,
    @Json(name = "PreviousValue")
    val previousValue: String?,
    @Json(name = "PersonalFastest")
    val personalFastest: Boolean?,
    @Json(name = "OverallFastest")
    val overallFastest: Boolean?,
    @Json(name = "Segments")
    val segments: Map<String, Int>?
)
