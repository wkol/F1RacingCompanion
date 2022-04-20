package com.example.f1racingcompanion.home

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class MeetingCountdown(
    val days: Long,
    val hours: Long,
    val minutes: Long,
) {
    constructor(start: ZonedDateTime, end: ZonedDateTime) : this(
        ChronoUnit.DAYS.between(start, end),
        ChronoUnit.HOURS.between(start, end) % 24,
        ChronoUnit.MINUTES.between(start, end) % 60
    )
}
