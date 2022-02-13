package com.example.f1racingcompanion.model

data class TimingAppData(
    val driver: F1Driver,
    val pitstopCount: Int,
    val currentTires: Tires?,
    val currentPos: Int?,
    val lapNumber: Int?,
    val lapTime: String?
)