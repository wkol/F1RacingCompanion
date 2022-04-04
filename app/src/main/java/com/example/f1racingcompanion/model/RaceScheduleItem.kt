package com.example.f1racingcompanion.model

import java.time.ZonedDateTime

data class RaceScheduleItem(
    val state: SessionState,
    val description: String,
    val zonedStartTime: ZonedDateTime,
) {
    enum class SessionState {
        UPCOMING,
        ONGOING,
        FINISHED
    }
}
