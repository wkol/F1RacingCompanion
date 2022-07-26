package com.example.f1racingcompanion.model

data class PositionData(
    val delayInMilis: Long,
    val position: List<PositionOnTrack>,
    val timestamp: Long
)
