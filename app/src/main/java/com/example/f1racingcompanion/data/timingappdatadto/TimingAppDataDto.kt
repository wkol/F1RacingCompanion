package com.example.f1racingcompanion.data.timingappdatadto

import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1Driver
import com.example.f1racingcompanion.model.TimingAppData
import com.example.f1racingcompanion.model.Tires
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimingAppDataDto(
    val time: String,
    val lapInfo: Map<Int, DriverTimingDto>
)
