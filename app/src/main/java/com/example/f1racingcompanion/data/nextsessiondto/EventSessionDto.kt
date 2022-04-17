package com.example.f1racingcompanion.data.nextsessiondto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventSessionDto(
    @Json(name = "Circuit")
    val circuitInfo: CircuitDto,
    @Json(name = "FirstPractice")
    val firstPractice: F1EventDto?,
    @Json(name = "SecondPractice")
    val secondPractice: F1EventDto?,
    @Json(name = "ThirdPractice")
    val thirdPractice: F1EventDto?,
    @Json(name = "Qualifying")
    val qualifying: F1EventDto?,
    @Json(name = "Sprint")
    val sprint: F1EventDto?,
    @Json(name = "raceName")
    val raceName: String,
    @Json(name = "date")
    val raceDate: String,
    @Json(name = "time")
    val raceTime: String,
)
