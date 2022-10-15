package com.example.f1racingcompanion.timing

import com.example.f1racingcompanion.model.F1DriverListElement

data class TimingPreviewData(
    val standing: List<F1DriverListElement>,
    val fastestLap: FastestRaceLap,
    val driversPositions: List<Position>,
    val circuitInfo: CircuitInfo,
    val sessionType: String,
    val isLoading: Boolean,
    val telemetry: Telemetry,
    val isTelemetryOpen: Boolean = false,
    val expandedState: ExpandedState = ExpandedState(standing.map { it.carNumber })
)
