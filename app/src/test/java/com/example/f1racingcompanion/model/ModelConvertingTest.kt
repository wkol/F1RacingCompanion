package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.nextsessiondto.CircuitDto
import com.example.f1racingcompanion.data.nextsessiondto.EventSessionDto
import com.example.f1racingcompanion.data.nextsessiondto.F1EventDto
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
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
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
                    null,
                    null,
                    SectorValue("1:18:10.1", null, true, false, null),
                    null,
                    true,
                    false,
                    false,
                    false,
                    15,
                    12,
                    1,
                    emptyList(),
                    false
                )
            ),
            null
        )

        // When converting it to Timing
        val timing = timingData.toListTimingData()

        // Then Timing consits of correctly parsed timingDataDto
        assertEquals(33, timing[0].driverNum)
        assertEquals("1:18:10.1", timing[0].lastLapTime)
        assertEquals("+12.99", timing[0].gapToLeader)
        assertEquals("3.24", timing[0].gapToNext)
        assertEquals(15, timing[0].position)
    }

    @Test
    fun convertDifferentStructureTimingData() {
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
                    null,
                    null,
                    "+3.1",
                    "+10.99",
                    SectorValue("1:18:10.1", null, true, false, null),
                    null,
                    true,
                    false,
                    false,
                    false,
                    15,
                    12,
                    1,
                    emptyList(),
                    false
                )
            ),
            null
        )

        // When converting it to Timing
        val timing = timingData.toListTimingData()

        // Then Timing consits of correctly parsed timingDataDto
        assertEquals(33, timing[0].driverNum)
        assertEquals("1:18:10.1", timing[0].lastLapTime)
        assertEquals("+10.99", timing[0].gapToLeader)
        assertEquals("+3.1", timing[0].gapToNext,)
        assertEquals(15, timing[0].position)
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
                    null, null, null, null, null, null, null, null, null, null, null, null, null, null, emptyList(), null
                )
            ),
            null
        )

        // When converting it to Timing
        val timing = timingData.toListTimingData()

        // Then converter should ignore useless data
        assertTrue(timing.isEmpty())
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
                )
            )
        )

        // When converting it to Timing
        val timingAppData = timingAppDataDto.toListTimingAppData()

        // Then converter should ignore useless data
        assertEquals(33, timingAppData[0].driverNum)
        assertEquals("1:12:11.1", timingAppData[0].lapTime)
        assertEquals(Tires(Compound.MEDIUM, false, 2), timingAppData[0].currentTires)
        assertEquals(11, timingAppData[0].position)
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
                        0,
                        null,
                        null,
                        emptyList(),
                        false
                    )
                ),
                null
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
        assertEquals(2, f1DriverList.size)
        assertEquals(31, f1DriverList[0].carNumber)
        assertEquals(10, f1DriverList[0].position)
        assertEquals(-1, f1DriverList[1].position)
    }

    @Test
    fun convertEventTrackerDtoToNextSession() {
        val eventSession = EventSessionDto(
            CircuitDto("Albert Park Grand Prix Circuit", "albert_park"),
            F1EventDto(date = "2022-04-08", time = "03:00:00Z"),
            F1EventDto(date = "2022-04-08", time = "06:00:00Z"),
            F1EventDto(date = "2022-04-09", time = "03:00:00Z"),
            F1EventDto(date = "2022-04-09", time = "06:00:00Z"),
            null,
            "Australian Grand Prix",
            "2022-04-10",
            "06:00:00Z"
        )

        val nextSession = eventSession.toNextSession()

        assertEquals("Australian Grand Prix", nextSession.raceName)
        assertEquals("Albert Park Grand Prix Circuit", nextSession.circuitName)
        assertEquals(
            ZonedDateTime.of(LocalDate.of(2022, 4, 8), LocalTime.of(3, 0, 0), ZoneId.of("UTC")).toInstant(),
            nextSession.schedule[0].zonedStartTime.toInstant()
        )
        assertEquals(false, nextSession.schedule[1].isUpcoming)
    }
}
