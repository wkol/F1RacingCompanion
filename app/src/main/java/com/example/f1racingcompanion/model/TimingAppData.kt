package com.example.f1racingcompanion.model

data class TimingAppData(
    val driverNum: Int,
    val currentTires: Tires?,
    val lapNumber: Int?,
    val lapTime: String?,
    val position: Int?
)
