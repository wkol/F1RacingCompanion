package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.SectorValue

class F1DriverListElement(
    var lapTime: String,
    var lastSectors: MutableMap<String, SectorValue>,
    var tires: Tires,
    var position: Int,
    var interval: String,
    var toFirst: String,
    var inPit: Boolean,
    num: Int
) : F1Driver(getDriverByNumber(num)) {


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
