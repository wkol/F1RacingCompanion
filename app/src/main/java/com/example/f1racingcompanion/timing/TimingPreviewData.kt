package com.example.f1racingcompanion.timing

import com.example.f1racingcompanion.model.F1DriverListElement

data class TimingPreviewData(
    val standing: List<F1DriverListElement>,
    val fastestLap: FastestRaceLap,
    val driversPositions: Map<Int, Position>,
    val circuitInfo: CircuitInfo,
    val isLoading: Boolean
)
