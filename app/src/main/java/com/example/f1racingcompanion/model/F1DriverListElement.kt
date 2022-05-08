package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue

data class F1DriverListElement(
    var lastLapTime: String,
    var lastSectors: MutableMap<String, SectorValue>,
    var tires: Tires,
    var position: Int,
    var interval: String,
    var toFirst: String,
    var bestLap: BestLap,
    var inPit: Boolean,
    var retired: Boolean,
    var pitstopCount: Int,
    var startingGridPos: Int,
    var firstName: String,
    var lastName: String,
    var carNumber: Int,
    var shortcut: String,
    var team: String,
    var teamColor: Long,
    var isExpanded: Boolean = false
)
