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
    val interval: Interval?,
    @Json(name = "TimeDiffToPositionAhead")
    val timeDiffToNext: String?,
    @Json(name = "TimeDiffToFastest")
    val timeDiffToFastest: String?,
    @Json(name = "LastLapTime")
    val lastLap: SectorValue?,
    @Json(name = "BestLapTime")
    val bestLapTime: BestLap?,
    @Json(name = "InPit")
    val inPit: Boolean?,
    @Json(name = "PitOut")
    val outPit: Boolean?,
    @Json(name = "Stopped")
    val stopeed: Boolean?,
    @Json(name = "Retired")
    val retired: Boolean?,
    @Json(name = "Position")
    val position: Int?,
    @Json(name = "NumberOfLaps")
    val lapsNum: Int?,
    @Json(name = "NumberOfPitstops")
    val pitsNum: Int?,
    @Json(name = "Stats")
    val qualyfingStats: Map<Int, QualifyingStats>?,
    @Json(name = "KnockedOut")
    val knockedOut: Boolean?
)
