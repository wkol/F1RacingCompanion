package com.example.f1racingcompanion.home

import com.example.f1racingcompanion.model.NextSession
import java.time.ZonedDateTime

data class RaceStatusState(
    val isActive: Boolean? = null,
    val isLoading: Boolean = false,
    val nextSession: NextSession? = null,
    val countdown: MeetingCountdown? = null,
    val error: String = ""
) {
    constructor(isActive: Boolean, isLoading: Boolean, nextSession: NextSession) : this(
        isActive = isActive,
        isLoading = isLoading,
        nextSession = nextSession,
        countdown = MeetingCountdown(
            ZonedDateTime.now(),
            nextSession.schedule.firstOrNull()?.zonedStartTime ?: ZonedDateTime.now()
        )
    )
}
