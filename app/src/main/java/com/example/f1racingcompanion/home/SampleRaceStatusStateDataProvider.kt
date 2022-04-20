package com.example.f1racingcompanion.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.f1racingcompanion.model.NextSession
import com.example.f1racingcompanion.model.RaceScheduleItem
import java.time.ZonedDateTime

class SampleRaceStatusStateDataProvider : PreviewParameterProvider<RaceStatusState> {
    override val values: Sequence<RaceStatusState>
        get() = sequenceOf(
            RaceStatusState(
                isActive = false,
                isLoading = false,
                nextSession = NextSession(
                    circuitName = "VEr VERY LOGN CIRUCasdasdsdsdsdsdsdsdsdsdsdsdsdT NAM Name",
                    raceName = "Circuit country",
                    schedule = listOf(
                        RaceScheduleItem(
                            isUpcoming = true,
                            description = "Qualifying",
                            zonedStartTime = ZonedDateTime.now()
                        )
                    ),
                    circuitId = "1"
                ),
                countdown = MeetingCountdown(10, 10, 10)
            ),
            RaceStatusState(
                isActive = null,
                isLoading = true,
            ),
            RaceStatusState(
                isActive = null,
                isLoading = false,
                error = "Error message"
            )

        )
}
