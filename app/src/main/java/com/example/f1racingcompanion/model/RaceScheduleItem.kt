package com.example.f1racingcompanion.model

import java.time.ZonedDateTime

data class RaceScheduleItem(
    val isUpcoming: Boolean,
    val description: String,
    val zonedStartTime: ZonedDateTime,
)
