package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.SectorValue

data class TelemetryInfo(
    val delayInMilis: Long,
    val currentSpeed: Int,
    val currentRPMValue: Int,
    val isDRSEnabled: Boolean,
    val currentThrottleValue: Int,
    val currentBrakeValue: Int,
    val currentGear: Byte,
    val driverStr: String?,
    val sectors: Map<String, SectorValue>
)
