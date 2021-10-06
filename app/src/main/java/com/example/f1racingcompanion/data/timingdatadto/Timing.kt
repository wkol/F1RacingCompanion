package com.example.f1racingcompanion.data.timingdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Timing(
    @Json(name = "Sectors")
    val sector: Map<String, SectorValue>?,
    @Json(name = "Speeds")
    val speeds: Map<String, SectorValue>?,
    @Json(name = "GapToLeader")
    val gap: String?,
    @Json(name = "IntervalToPositionAhead")
    val interval: Interval?
    )

