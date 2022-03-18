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
) {
    fun updateLastSectors(sector: Map<String, SectorValue>) {

        if (sector.contains("1")) {
            lastSectors.clear()
        }
        lastSectors.plus(sector)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is F1DriverListElement) return false
        if (!super.equals(other)) return false

        if (lastLapTime != other.lastLapTime) return false
        if (lastSectors != other.lastSectors) return false
        if (tires != other.tires) return false
        if (position != other.position) return false
        if (interval != other.interval) return false
        if (toFirst != other.toFirst) return false
        if (bestLap != other.bestLap) return false
        if (inPit != other.inPit) return false
        if (retired != other.retired) return false
        if (pitstopCount != other.pitstopCount) return false
        if (startingGridPos != other.startingGridPos) return false
        if (isExpanded != other.isExpanded) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + lastLapTime.hashCode()
        result = 31 * result + lastSectors.hashCode()
        result = 31 * result + tires.hashCode()
        result = 31 * result + position
        result = 31 * result + interval.hashCode()
        result = 31 * result + toFirst.hashCode()
        result = 31 * result + bestLap.hashCode()
        result = 31 * result + inPit.hashCode()
        result = 31 * result + retired.hashCode()
        result = 31 * result + pitstopCount
        result = 31 * result + startingGridPos
        result = 31 * result + isExpanded.hashCode()
        return result
    }
}
