package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.SectorValue

class F1DriverListElement(
    var lapTime: String,
    val lastSectors: MutableMap<String, SectorValue>,
    val tires: Tires
) : F1Driver() {
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
