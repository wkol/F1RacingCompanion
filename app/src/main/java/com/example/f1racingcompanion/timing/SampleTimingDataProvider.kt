package com.example.f1racingcompanion.timing

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.CircuitOffset
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.Tires
import com.example.f1racingcompanion.utils.Constants
import kotlinx.collections.immutable.persistentMapOf

private val driversNames = listOf(
    F1DriverListElement(
        "1:15:35",
        mutableMapOf(
            "0" to SectorValue(
                "15.43",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.SOFT, isNew = false, tyreAge = 9),
        position = 1,
        interval = "+3.05",
        toFirst = "+0.00",
        bestLap = BestLap(time = "1:40:18", lapNum = 3),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 3,
        firstName = "George",
        lastName = "Russell",
        carNumber = 63,
        shortcut = "RUS",
        team = "Mercedes",
        teamColor = 0xFF6cd3bf,
    ),
    F1DriverListElement(
        "1:12:45",
        mutableMapOf(
            "0" to SectorValue(
                "12.62",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.INTERMEDIATE, isNew = false, tyreAge = 10),
        position = 2,
        interval = "+6.77",
        toFirst = "+3.05",
        bestLap = BestLap(time = "1:27:50", lapNum = 6),
        inPit = false,
        retired = false,
        pitstopCount = 5,
        startingGridPos = 5,
        firstName = "Carlos",
        lastName = "Sainz",
        carNumber = 55,
        shortcut = "SAI",
        team = "Ferrari",
        teamColor = 0xFFed1c24,
    ),
    F1DriverListElement(
        "1:37:6",
        mutableMapOf(
            "0" to SectorValue(
                "12.34",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "14.5",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.INTERMEDIATE, isNew = false, tyreAge = 7),
        position = 3,
        interval = "+3.08",
        toFirst = "+9.81",
        bestLap = BestLap(time = "1:13:22", lapNum = 6),
        inPit = false,
        retired = false,
        pitstopCount = 5,
        startingGridPos = 5,
        firstName = "Charles",
        lastName = "Leclerc",
        carNumber = 16,
        shortcut = "LEC",
        team = "Ferrari",
        teamColor = 0xFFed1c24,
    ),
    F1DriverListElement(
        "1:15:38",
        mutableMapOf(
            "0" to SectorValue(
                "13.78",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.MEDIUM, isNew = false, tyreAge = 14),
        position = 4,
        interval = "+3.33",
        toFirst = "+12.89",
        bestLap = BestLap(time = "1:25:4", lapNum = 4),
        inPit = false,
        retired = false,
        pitstopCount = 4,
        startingGridPos = 1,
        firstName = "Lando",
        lastName = "Norris",
        carNumber = 4,
        shortcut = "NOR",
        team = "McLaren",
        teamColor = 0xFFf58020,
    ),
    F1DriverListElement(
        "1:17:29",
        mutableMapOf(
            "0" to SectorValue(
                "15.85",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "13.67",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.SOFT, isNew = false, tyreAge = 6),
        position = 5,
        interval = "+2.46",
        toFirst = "+16.22",
        bestLap = BestLap(time = "1:27:45", lapNum = 4),
        inPit = false,
        retired = false,
        pitstopCount = 1,
        startingGridPos = 4,
        firstName = "Esteban",
        lastName = "Ocon",
        carNumber = 31,
        shortcut = "OCO",
        team = "Alpine",
        teamColor = 0xFF2293d1,
    ),
    F1DriverListElement(
        "1:13:40",
        mutableMapOf(
            "0" to SectorValue(
                "12.18",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "12.18",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.HARD, isNew = false, tyreAge = 10),
        position = 6,
        interval = "+3.47",
        toFirst = "+18.68",
        bestLap = BestLap(time = "1:21:1", lapNum = 28),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 2,
        firstName = "Fernando",
        lastName = "Alonso",
        carNumber = 14,
        shortcut = "ALO",
        team = "Alpine",
        teamColor = 0xFF2293d1,
    ),
    F1DriverListElement(
        "1:28:23",
        mutableMapOf(
            "0" to SectorValue(
                "14.86",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.INTERMEDIATE, isNew = false, tyreAge = 3),
        position = 7,
        interval = "+3.07",
        toFirst = "+22.15",
        bestLap = BestLap(time = "1:22:3", lapNum = 16),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 2,
        firstName = "Lewis",
        lastName = "Hamilton",
        carNumber = 44,
        shortcut = "HAM",
        team = "Mercedes",
        teamColor = 0xFF6cd3bf,
    ),
    F1DriverListElement(
        "1:32:4",
        mutableMapOf(
            "0" to SectorValue(
                "13.86",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "15.75",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.HARD, isNew = false, tyreAge = 13),
        position = 8,
        interval = "+0.43",
        toFirst = "+25.23",
        bestLap = BestLap(time = "1:15:39", lapNum = 23),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 4,
        firstName = "Valtteri",
        lastName = "Bottas",
        carNumber = 77,
        shortcut = "BOT",
        team = "Alfa Romeo",
        teamColor = 0xFFb12039,
    ),
    F1DriverListElement(
        "1:39:45",
        mutableMapOf(
            "0" to SectorValue(
                "14.20",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.WET, isNew = false, tyreAge = 13),
        position = 9,
        interval = "+1.32",
        toFirst = "+25.65",
        bestLap = BestLap(time = "1:33:25", lapNum = 28),
        inPit = false,
        retired = false,
        pitstopCount = 5,
        startingGridPos = 5,
        firstName = "Daniel",
        lastName = "Ricciardo",
        carNumber = 3,
        shortcut = "RIC",
        team = "McLaren",
        teamColor = 0xFFf58020,
    ),
    F1DriverListElement(
        "1:32:46",
        mutableMapOf(
            "0" to SectorValue(
                "13.66",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "14.34",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.MEDIUM, isNew = false, tyreAge = 3),
        position = 10,
        interval = "+0.62",
        toFirst = "+26.97",
        bestLap = BestLap(time = "1:40:5", lapNum = 12),
        inPit = false,
        retired = false,
        pitstopCount = 4,
        startingGridPos = 2,
        firstName = "Max",
        lastName = "Verstappen",
        carNumber = 1,
        shortcut = "VER",
        team = "Red Bull Racing",
        teamColor = 0xFF1e5bc6,
    ),
    F1DriverListElement(
        "1:33:13",
        mutableMapOf(
            "0" to SectorValue(
                "12.35",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.MEDIUM, isNew = false, tyreAge = 7),
        position = 11,
        interval = "+3.72",
        toFirst = "+27.59",
        bestLap = BestLap(time = "1:16:39", lapNum = 8),
        inPit = false,
        retired = false,
        pitstopCount = 3,
        startingGridPos = 3,
        firstName = "Sergio",
        lastName = "Perez",
        carNumber = 11,
        shortcut = "PER",
        team = "Red Bull Racing",
        teamColor = 0xFF1e5bc6,
    ),
    F1DriverListElement(
        "1:18:18",
        mutableMapOf(
            "0" to SectorValue(
                "12.29",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "13.58",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.MEDIUM, isNew = false, tyreAge = 11),
        position = 12,
        interval = "+8.43",
        toFirst = "+31.31",
        bestLap = BestLap(time = "1:40:38", lapNum = 9),
        inPit = false,
        retired = false,
        pitstopCount = 1,
        startingGridPos = 1,
        firstName = "Guanyu",
        lastName = "Zhou",
        carNumber = 24,
        shortcut = "ZHO",
        team = "Alfa Romeo",
        teamColor = 0xFFb12039,
    ),
    F1DriverListElement(
        "1:15:40",
        mutableMapOf(
            "0" to SectorValue(
                "15.41",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.SOFT, isNew = false, tyreAge = 15),
        position = 13,
        interval = "+5.54",
        toFirst = "+39.74",
        bestLap = BestLap(time = "1:21:42", lapNum = 29),
        inPit = false,
        retired = false,
        pitstopCount = 3,
        startingGridPos = 2,
        firstName = "Kevin",
        lastName = "Magnussen",
        carNumber = 20,
        shortcut = "MAG",
        team = "Haas F1 Team",
        teamColor = 0xFFb6babd,
    ),
    F1DriverListElement(
        "1:31:35",
        mutableMapOf(
            "0" to SectorValue(
                "13.15",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "12.46",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "2" to SectorValue(
                "12.46",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.INTERMEDIATE, isNew = false, tyreAge = 12),
        position = 14,
        interval = "+7.91",
        toFirst = "+45.28",
        bestLap = BestLap(time = "1:24:23", lapNum = 16),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 1,
        firstName = "Lance",
        lastName = "Stroll",
        carNumber = 18,
        shortcut = "STR",
        team = "Aston Martin",
        teamColor = 0xFF2d826d,
    ),
    F1DriverListElement(
        "1:30:9",
        mutableMapOf(
            "0" to SectorValue(
                "14.5",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "13.27",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.INTERMEDIATE, isNew = false, tyreAge = 6),
        position = 15,
        interval = "+4.00",
        toFirst = "+53.19",
        bestLap = BestLap(time = "1:15:7", lapNum = 25),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 4,
        firstName = "Mick",
        lastName = "Schumacher",
        carNumber = 47,
        shortcut = "MSC",
        team = "Haas F1 Team",
        teamColor = 0xFFb6babd,
    ),
    F1DriverListElement(
        "1:22:33",
        mutableMapOf(
            "0" to SectorValue(
                "13.84",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.WET, isNew = false, tyreAge = 17),
        position = 16,
        interval = "+0.35",
        toFirst = "+57.18",
        bestLap = BestLap(time = "1:26:36", lapNum = 8),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 5,
        firstName = "Yuki",
        lastName = "Tsunoda",
        carNumber = 22,
        shortcut = "TSU",
        team = "AlphaTauri",
        teamColor = 0xFF4e7c9b,
    ),
    F1DriverListElement(
        "1:38:44",
        mutableMapOf(
            "0" to SectorValue(
                "15.18",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.SOFT, isNew = false, tyreAge = 3),
        position = 17,
        interval = "+1.57",
        toFirst = "+57.53",
        bestLap = BestLap(time = "1:24:10", lapNum = 17),
        inPit = false,
        retired = false,
        pitstopCount = 0,
        startingGridPos = 4,
        firstName = "Alexander",
        lastName = "Albon",
        carNumber = 23,
        shortcut = "ALB",
        team = "Williams",
        teamColor = 0xFF37bedd,
    ),
    F1DriverListElement(
        "1:32:45",
        mutableMapOf(
            "0" to SectorValue(
                "12.94",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "12.53",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.WET, isNew = false, tyreAge = 2),
        position = 18,
        interval = "+2.77",
        toFirst = "+59.10",
        bestLap = BestLap(time = "1:26:24", lapNum = 3),
        inPit = false,
        retired = false,
        pitstopCount = 1,
        startingGridPos = 5,
        firstName = "Sebastian",
        lastName = "Vettel",
        carNumber = 5,
        shortcut = "VET",
        team = "Aston Martin",
        teamColor = 0xFF2d826d,
    ),
    F1DriverListElement(
        "1:22:41",
        mutableMapOf(
            "0" to SectorValue(
                "15.6",
                personalFastest = false,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
            "1" to SectorValue(
                "13.43",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.SOFT, isNew = false, tyreAge = 9),
        position = 19,
        interval = "+5.18",
        toFirst = "+61.88",
        bestLap = BestLap(time = "1:40:21", lapNum = 24),
        inPit = false,
        retired = true,
        pitstopCount = 3,
        startingGridPos = 3,
        firstName = "Nicholas",
        lastName = "Latifi",
        carNumber = 6,
        shortcut = "LAT",
        team = "Williams",
        teamColor = 0xFF37bedd,
    ),
    F1DriverListElement(
        "1:33:23",
        mutableMapOf(
            "0" to SectorValue(
                "13.26",
                personalFastest = true,
                overallFastest = true,
                previousValue = null,
                segments = null
            ),
        ),
        Tires(currentCompound = Compound.MEDIUM, isNew = false, tyreAge = 16),
        position = 20,
        interval = "+2.20",
        toFirst = "+67.06",
        bestLap = BestLap(time = "1:40:33", lapNum = 34),
        inPit = false,
        retired = true,
        pitstopCount = 4,
        startingGridPos = 1,
        firstName = "Pierre",
        lastName = "Gasly",
        carNumber = 10,
        shortcut = "GAS",
        team = "AlphaTauri",
        teamColor = 0xFF4e7c9b,
    )
)

class SampleTimingDataProvider : PreviewParameterProvider<TimingPreviewData> {
    override val values: Sequence<TimingPreviewData>
        get() = sequenceOf(
            TimingPreviewData(
                standing = driversNames,
                fastestLap = FastestRaceLap(1, "1:33:11"),
                driversPositions = listOf(
                    Position(
                        0xFFFFFFFF,
                        Offset(-2980 + 580F, 5058 + 8358F)
                    )
                ),
                circuitInfo = Constants.CIRCUITS["bahrain"]!!,
                isLoading = false,
                sessionType = "FP1",
                telemetry = Telemetry(null, 333, 2000, 8, Animatable(0.5F), Animatable(0.2F), true, persistentMapOf()),
            ),
            TimingPreviewData(
                standing = driversNames,
                fastestLap = FastestRaceLap(1, "1:33:11"),
                driversPositions = listOf(
                    Position(
                        0xFFFFFFFF,
                        Offset(-2980 + 580F, 5058 + 8358F)
                    )
                ),
                circuitInfo = Constants.CIRCUITS["bahrain"]!!,
                isLoading = false,
                sessionType = "FP1",
                telemetry = Telemetry(
                    driverStr = "LEC",
                    speed = 333,
                    rpm = 2000,
                    gear = 8,
                    brake = Animatable(0.5F),
                    throttle = Animatable(0.2F),
                    isDrs = true,
                    sectors = persistentMapOf(
                        "0" to SectorValue(
                            "13.263",
                            personalFastest = true,
                            overallFastest = true,
                            previousValue = null,
                            segments = null
                        ),
                        "1" to SectorValue(
                            "21.262",
                            personalFastest = true,
                            overallFastest = true,
                            previousValue = null,
                            segments = null
                        ),
                        "2" to SectorValue(
                            "21.11",
                            personalFastest = true,
                            overallFastest = true,
                            previousValue = null,
                            segments = null
                        ),
                    ),
                ),
                isTelemetryOpen = true
            ),
            TimingPreviewData(
                standing = emptyList(),
                fastestLap = FastestRaceLap(-1, "-"),
                driversPositions = emptyList(),
                circuitInfo = CircuitInfo(CircuitOffset(0F, 0F, 0F, 0F), "-", R.drawable.bahrain),
                sessionType = "FP1",
                isLoading = true,
                telemetry = Telemetry(null, 333, 2000, 8, Animatable(0.5F), Animatable(0.2F), true, persistentMapOf()),
            )
        )
}
