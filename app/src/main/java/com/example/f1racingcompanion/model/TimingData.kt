package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue

data class TimingData(
    val driverNum: Int,
    val gapToLeader: String?,
    val gapToNext: String?,
    val lastLapTime: String?,
    val fastestLap: BestLap?,
    val sector: Map<String, SectorValue>?,
    val position: Int?,
    val inPit: Boolean?,
    val retired: Boolean?,
    val pits: Int?,
    val overallFastest: Boolean?
)
