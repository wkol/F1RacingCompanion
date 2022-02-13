package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.SectorValue

data class TimingData(
    val driver: F1Driver,
    val gapToLeader: String?,
    val gapToNext: String?,
    val lastLapTime: String?,
    val fastestLap: Boolean?,
    val sector: Map<String, SectorValue>?,
)



