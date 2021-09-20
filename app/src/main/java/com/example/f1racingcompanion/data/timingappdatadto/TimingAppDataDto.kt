package com.example.f1racingcompanion.data.timingappdatadto

data class TimingAppDataDto(
    val time: String,
    val lapInfo: Map<Int, DriverTimingDto>
)
