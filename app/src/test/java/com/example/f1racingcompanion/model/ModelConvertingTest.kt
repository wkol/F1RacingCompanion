package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingappdatadto.DriverTimingDto
import com.example.f1racingcompanion.data.timingappdatadto.Stint
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.Interval
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.data.timingdatadto.Timing
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.utils.toListTimingAppData
import com.example.f1racingcompanion.utils.toListTimingData
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelConvertingTest {

    @Test
    fun convertSuccesfullyToTimingData() {
        // Given a timingData object
        val timingData = TimingDataDto(
            mapOf(
                33 to Timing(
                    mapOf(
                        "1" to SectorValue(
                            "22.13",
                            null,
                            true,
                            null,
                            null
                        )
                    ),
                    mapOf(),
                    "+12.99",
                    Interval("3.24", false),
                    SectorValue("1:18:10.1", null, true, false, null),
                    null,
                    true,
                    false,
                    false,
                    false,
                    15,
                    12,
                    1
                )
            )
        )

        // When converting it to Timing
        val timing = timingData.toListTimingData()

        // Then Timing consits of correctly parsed timingDataDto
        assertEquals(timing[0].driverNum, 33)
        assertEquals(timing[0].lastLapTime, "1:18:10.1")
        assertEquals(timing[0].gapToLeader, "+12.99")
        assertEquals(timing[0].gapToNext, "3.24")
        assertEquals(timing[0].position, 15)
    }

    @Test
    fun convertIgnoreTimingData() {
        // Given a timingData object with is nothing but segment value
        val timingData = TimingDataDto(
            mapOf(
                33 to Timing(
                    mapOf(
                        "1" to SectorValue(
                            null,
                            null,
                            null,
                            null,
                            mapOf("Status" to 2048)
                        )
                    ),
                    null, null, null, null, null, null, null, null, null, null, null, null
                )
            )
        )

        // When converting it to Timing
        val timing = timingData.toListTimingData()

        // Then converter should ignore useless data
        assertEquals(timing.isEmpty(), true)
    }

    @Test
    fun convertSuccesfullyToTimingAppData() {
        // Given a timingAppData object
        val timingAppDataDto = TimingAppDataDto(
            mapOf(
                33 to DriverTimingDto(
                    mapOf(
                        1 to Stint(
                            null,
                            "1:12:11.1",
                            22,
                            "MEDIUM",
                            "false",
                            2
                        )
                    ),
                    11,
                    "12"
                )
            )
        )

        // When converting it to Timing
        val timingAppData = timingAppDataDto.toListTimingAppData()

        // Then converter should ignore useless data
        assertEquals(timingAppData[0].driverNum, 33)
        assertEquals(timingAppData[0].lapTime, "1:12:11.1")
        assertEquals(timingAppData[0].startingGridPos, 12)
        assertEquals(timingAppData[0].currentTires, Tires(Compound.MEDIUM, false, 2))
    }
}
