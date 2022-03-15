package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue

class F1DriverListElement(
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
    firstName: String,
    lastName: String,
    carNumber: Int,
    shortcut: String,
    team: String,
    teamColor: Long
) : F1Driver(firstName, lastName, carNumber, shortcut, team, teamColor) {

    fun updateLastSectors(sector: Map<String, SectorValue>) {
        if (sector.contains("1")) {
            lastSectors.clear()
        }
        lastSectors.plus(sector)
    }
}
