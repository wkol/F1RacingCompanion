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
    @Json(name = "TimeDiffToPositionAhead")
    val interval: String?,
    @Json(name = "LastLapTime")
    val lastLap: SectorValue?,
    @Json(name = "TimeDiffToFastest")
    val timeDiffFastest: String?,
    @Json(name = "InPit")
    val inPit: Boolean?,
    @Json(name = "PitOut")
    val outPit: Boolean?
    )

