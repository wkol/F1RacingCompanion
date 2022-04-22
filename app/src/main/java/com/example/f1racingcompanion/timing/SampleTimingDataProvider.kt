package com.example.f1racingcompanion.timing

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

class SampleTimingDataProvider : PreviewParameterProvider<TimingPreviewData> {
    override val values: Sequence<TimingPreviewData>
        get() = sequenceOf(
            TimingPreviewData(
                standing = List(20) {
                    F1DriverListElement(
                        "1:22:11",
                        mutableMapOf(
                            "1" to SectorValue(
                                "13.78",
                                personalFastest = false,
                                overallFastest = true,
                                previousValue = null,
                                segments = null
                            ),
                            "2" to SectorValue(
                                "13.78",
                                personalFastest = false,
                                overallFastest = false,
                                segments = null,
                                previousValue = null
                            )
                        ),
                        Tires(currentCompound = Compound.HARD, isNew = false, tyreAge = 2),
                        position = it + 1,
                        interval = "+1,4",
                        toFirst = "+9.0",
                        bestLap = BestLap(time = "1:11:11", lapNum = 11),
                        inPit = false,
                        retired = false,
                        pitstopCount = 0,
                        startingGridPos = 1,
                        firstName = "a",
                        lastName = "b",
                        carNumber = it + 1,
                        shortcut = "AAB",
                        team = "RED Bull",
                        teamColor = 0xFFFFFFFF
                    )
                },
                fastestLap = FastestRaceLap(1, "1:33:11"),
                driversPositions = mapOf(
                    33 to Position(
                        0xFFFFFFFF,
                        Offset(-2980 + 580F, 5058 + 8358F)
                    )
                ),
                circuitInfo = Constants.CIRCUITS["bahrain"]!!,
                isLoading = false
            ),
            TimingPreviewData(
                emptyList(), FastestRaceLap(-1, "-"), emptyMap(),
                CircuitInfo(CircuitOffset(0F, 0F, 0F, 0F), "-", R.drawable.bahrain), true
            )
        )
}
