package com.example.f1racingcompanion.model

import androidx.compose.ui.graphics.Color
import com.example.f1racingcompanion.data.timingdatadto.SectorValue

data class F1Driver(
    val firstName: String,
    val lastName: String,
    val carNumber: Int,
    val shortcut: String,
    val team: String,
    var lapTime: String,
    val lastSectors: MutableMap<String, SectorValue>,
    val teamColor: TeamColor,
    val tires: Tires
) {
    fun updateLastSectors(sector: Map<String, SectorValue>) {
        if (sector.contains("1")) {
            lastSectors.clear()
        }
        lastSectors.plus(sector)
    }

    fun updateLapTime(lastLap: SectorValue) {
        lapTime = lastLap.value!!
    }
}
