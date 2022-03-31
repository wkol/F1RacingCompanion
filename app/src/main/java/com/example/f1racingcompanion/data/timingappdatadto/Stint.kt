package com.example.f1racingcompanion.data.timingappdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stint(
    @Json(name = "LapFlags")
    val lapFlags: Int?,
    @Json(name = "LapTime")
    val lapTime: String?,
    @Json(name = "LapNumber")
    val lapNumber: Int?,
    @Json(name = "Compound")
    val compound: String?,
    @Json(name = "New")
    val newTires: String?,
    @Json(name = "TotalLaps")
    val tiresAge: Int?,
)

//        Compound: HARD
//        LapFlags: 0
//        New: 'true'
//        StartLaps: 0
//        TotalLaps: 0
//        TyresNotChanged: '0'
