package com.example.f1racingcompanion.data.f1driverlistelementdto

import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.Tires
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class F1DriverListElementDto(
    var lastLapTime: String,
    var lastSectors: Map<String, SectorValue>,
    var tires: Tires,
    var position: Int,
    var interval: String,
    var toFirst: String,
    var bestLap: BestLap,
    var inPit: Boolean,
    var retired: Boolean,
    var pitstopCount: Int,
    var startingGridPos: Int,
    val firstName: String,
    val lastName: String,
    val carNumber: Int,
    val shortcut: String,
    val team: String,
    val teamColor: Long
)
