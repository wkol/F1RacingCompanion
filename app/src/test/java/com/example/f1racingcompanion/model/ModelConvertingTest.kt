package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.nextsessiondto.EventTrackerDto
import com.example.f1racingcompanion.data.nextsessiondto.NextSessionDto
import com.example.f1racingcompanion.data.nextsessiondto.RaceInfoDto
import com.example.f1racingcompanion.data.previousdata.DriverInfoDto
import com.example.f1racingcompanion.data.previousdata.PreviousData
import com.example.f1racingcompanion.data.previousdata.PreviousDriverTimingDto
import com.example.f1racingcompanion.data.previousdata.PreviousTiming
import com.example.f1racingcompanion.data.previousdata.PreviousTimingAppDataDto
import com.example.f1racingcompanion.data.previousdata.PreviousTimingDataDto
import com.example.f1racingcompanion.data.timingappdatadto.DriverTimingDto
import com.example.f1racingcompanion.data.timingappdatadto.Stint
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.Interval
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.data.timingdatadto.Timing
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.utils.toF1DriverListElementList
import com.example.f1racingcompanion.utils.toListTimingAppData
import com.example.f1racingcompanion.utils.toListTimingData
import com.example.f1racingcompanion.utils.toNextSession
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

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

    @Test
    fun convertPreviousDataSuccesfullyToF1DriverListElemetns() {
        val previousData = PreviousData(
            PreviousTimingAppDataDto(
                mapOf(
                    31 to PreviousDriverTimingDto(
                        listOf(
                            Stint(
                                null,
                                "1:13:11.1",
                                21,
                                "SOFT",
                                "true",
                                3
                            )
                        ),
                        11,
                        "12"
                    )
                )
            ),
            PreviousTimingDataDto(
                mapOf(
                    31 to PreviousTiming(
                        listOf(
                            SectorValue(
                                "34.165",
                                null,
                                false,
                                false,
                                mapOf("Status" to 2048)
                            )
                        ),
                        "+1.0",
                        Interval("+12.0", false),
                        SectorValue("1:12:00", null, true, true, null),
                        BestLap("1:12:00", 21),
                        false,
                        false,
                        false,
                        false,
                        10,
                        20,
                        0
                    )
                )
            ),
            mapOf(
                31 to DriverInfoDto(
                    "Max Verstappen",
                    "NED",
                    "Max",
                    "Max Verstappen",
                    "Verstappen",
                    31,
                    "-",
                    "ffffff",
                    "Red Bull",
                    "VER"
                ),
                33 to DriverInfoDto(
                    "Lewis Hamilton",
                    "ENG",
                    "Lewis",
                    "Lewis Hamilton",
                    "Hamilton",
                    33,
                    "-",
                    "6cd3bf",
                    "McLaren",
                    "HAM"
                )
            )
        )
        val f1DriverList = previousData.toF1DriverListElementList()
        assertEquals(f1DriverList.size, 2)
        assertEquals(f1DriverList[0].carNumber, 31)
        assertEquals(f1DriverList[0].position, 10)
        assertEquals(f1DriverList[1].position, -1)
    }

    @Test
    fun convertEventTrackerDtoToNextSession() {
        val eventTracker = EventTrackerDto(
            RaceInfoDto(
                "Australia",
                "FORMULA 1 HEINEKEN AUSTRALIAN GRAND PRIX 2022"
            ),
            listOf(
                NextSessionDto(
                    "Upcoming",
                    "+10",
                    "Race",
                    LocalDateTime.parse("2022-04-10T15:00:00")
                )
            )
        )

        val nextSession = eventTracker.toNextSession()

        assertEquals(nextSession.circuitcountry, "Australia")
        assertEquals(nextSession.circuitName, "FORMULA 1 HEINEKEN AUSTRALIAN GRAND PRIX 2022")
        assertEquals(
            nextSession.schedule[0].zonedStartTime.withZoneSameInstant(ZoneId.of("UTC")),
            ZonedDateTime.of(2022, 4, 10, 5, 0, 0, 0, ZoneId.of("UTC"))
        )
    }
}
