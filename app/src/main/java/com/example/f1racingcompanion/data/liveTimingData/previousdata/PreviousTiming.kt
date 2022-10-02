package com.example.f1racingcompanion.data.liveTimingData.previousdata

import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.Interval
import com.example.f1racingcompanion.data.timingdatadto.QualifyingStats
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PreviousTiming(
    @Json(name = "Sectors")
    val sector: List<SectorValue>?,
    @Json(name = "GapToLeader")
    val gap: String?,
    @Json(name = "IntervalToPositionAhead")
    val interval: Interval?,
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
    @Json(name = "TimeDiffToPositionAhead")
    val timeDiffToNext: String?,
    @Json(name = "TimeDiffToFastest")
    val timeDiffToFastest: String?,
    @Json(name = "Stats")
    val qualyfingStats: List<QualifyingStats>?,
    @Json(name = "KnockedOut")
    val knockedOut: Boolean?
)
