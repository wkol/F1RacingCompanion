package com.example.f1racingcompanion.data.f1driverlistelementdto

import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.Tires
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class F1DriverListElementDto(
    val lastLapTime: String,
    val lastSectors: Map<String, SectorValue>,
    val tires: Tires,
    val position: Int,
    val interval: String,
    val toFirst: String,
    val bestLap: BestLap,
    val inPit: Boolean,
    val retired: Boolean,
    val pitstopCount: Int,
    val startingGridPos: Int,
    val firstName: String,
    val lastName: String,
    val carNumber: Int,
    val shortcut: String,
    val team: String,
    val teamColor: Long
)
