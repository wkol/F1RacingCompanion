package com.example.f1racingcompanion.data.nextsessiondto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventTrackerDto(
    @Json(name = "race")
    val raceInfo: RaceInfoDto,
    @Json(name = "timetable")
    val timetable: List<NextSessionDto>
)
