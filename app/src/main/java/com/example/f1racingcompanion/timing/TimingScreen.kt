package com.example.f1racingcompanion.timing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.Tires
import com.example.f1racingcompanion.ui.theme.TitilliumWeb
import com.example.f1racingcompanion.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun DriverElement(
    modifier: Modifier = Modifier,
    driverListElement: F1DriverListElement,
    isInterval: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        PositionBox(
            position = driverListElement.position,
            modifier =
            Modifier
                .padding(start = 8.dp, top = 3.dp, end = 3.dp, bottom = 3.dp)
                .size(25.dp)
        )
        Spacer(Modifier.width(10.dp))
        TeamColorBox(driverListElement.teamColor)
        Text(
            text = "${driverListElement.firstName[0]}.${driverListElement.lastName}",
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentSize(Alignment.CenterStart)
                .padding(horizontal = 10.dp),
            color = Color.White,
            fontSize = 25.sp,
            fontFamily = TitilliumWeb,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isInterval) driverListElement.interval else driverListElement.toFirst,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterEnd)
                .padding(horizontal = 10.dp),
            color = Color.White,
            fontSize = 25.sp,
            fontFamily = TitilliumWeb,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CircuitDriverPlot(
    circuitInfo: CircuitInfo,
    driversPostitions: Map<Int, Position>,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        CircuitPlot(
            circuitName = circuitInfo.circuitName,
            modifier = Modifier
                .fillMaxSize(0.9F)
                .align(Alignment.Center)
        )
        DriverPlot(
            driversPosition = driversPostitions,
            offset = circuitInfo.circuitOffset,
            modifier = Modifier
                .fillMaxSize(0.9F)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun DriverDetails(modifier: Modifier = Modifier, driver: F1DriverListElement, color: Color) {
    Box(
        modifier
            .clip(
                RoundedCornerShape(
                    topStartPercent = 0,
                    topEndPercent = 20,
                    bottomEndPercent = 20,
                    bottomStartPercent = 0
                )
            )
            .background(color)
    ) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround) {
            TiresIndicator(modifier = Modifier.fillMaxHeight(), tires = driver.tires)
            LapTimeIndicator(modifier = Modifier.fillMaxHeight(), lapTime = driver.lastLapTime)
            SectorIndicator(modifier = Modifier.fillMaxHeight(), sectors = driver.lastSectors)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TimingScreen(viewModel: TimingViewModel) {
    val standing = viewModel.standing.collectAsState()
    val positions = viewModel.driversPosition.collectAsState()
    val fastestLap = viewModel.fastestLap.collectAsState()
    Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RaceNameText(
                raceName = viewModel.circuitInfo.fullName,
                modifier = Modifier.align(Alignment.Start)
            )
            StandingLazyList(
                standing = standing.value.values.filter{ it.position != 0 }.sortedBy { it.position }.toList(),
                fastestLap = fastestLap.value
            )
            Spacer(Modifier.height(10.dp))
            CircuitDriverPlot(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(80F))
                    .border(BorderStroke(2.dp, Color(0xFF474747)), RoundedCornerShape(80F))
                    .background(Color(0x57141330)),
                circuitInfo = viewModel.circuitInfo,
                driversPostitions = positions.value
            )
        }
    }
}

@Composable
fun StandingLazyList(
    standing: List<F1DriverListElement>,
    fastestLap: FastestRaceLap,
) {
    var isInterval by remember { mutableStateOf(false) }
    val expandedStates = remember(standing) {
        List(20) { false }.toMutableStateList()
    }
    StandingHeader(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 0,
                    bottomEndPercent = 0
                )
            )
            .background(Color.DarkGray),
        isInterval
    ) { isInterval = !isInterval }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.95F)
            .fillMaxSize(0.65F)
            .padding(2.dp)
    ) {
        itemsIndexed(standing, { _, it -> it.carNumber }) { idx, element ->
            DriverElement(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(
                        RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 20,
                            bottomEndPercent = 20,
                            bottomStartPercent = 0
                        )
                    )
                    .border(
                        BorderStroke(
                            2.dp,
                            if (fastestLap.driverNum == element.carNumber) Color(0xFFB124D5) else Color(
                                0xFF474747
                            )
                        ),
                        RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 20,
                            bottomEndPercent = 20,
                            bottomStartPercent = 0
                        )
                    )
                    .background(Color(0x60151735)),
                driverListElement = element,
                isInterval = isInterval
            ) {
                expandedStates[idx] = !expandedStates[idx]
            }
            AnimatedVisibility(expandedStates[idx]) {
                DriverDetails(
                    driver = element,
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(2.dp),
                    color = Color(0x57141330)
                )
            }
        }
    }
}

@Preview("TimingScreenPreview")
@Composable
@OptIn(ExperimentalCoroutinesApi::class)
fun DriverElementPreview() {
    val elements = remember {
        List(20) {
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
                Tires(Compound.HARD, false, 2), 1, "+1,4", "+9.0",
                BestLap("1:11:11", 11),
                false,
                false,
                0,
                1,
                "a",
                "b",
                it + 1,
                "AAB",
                "RED Bull",
                0xFFFFFFFF
            )
        }.toMutableStateList()
    }
    val ci = CircuitInfo("bahrain", Constants.OFFSETMAP["bahrain"]!!, "Bahrain")
    Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RaceNameText(raceName = ci.fullName, modifier = Modifier.align(Alignment.Start))
            StandingLazyList(standing = elements, fastestLap = FastestRaceLap(1, "1:11"))
            Spacer(Modifier.height(10.dp))
            CircuitDriverPlot(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(80F))
                    .border(BorderStroke(2.dp, Color(0xFF474747)), RoundedCornerShape(80F))
                    .background(Color(0x57141330)),
                circuitInfo = ci,
                driversPostitions = mapOf(
                    33 to Position(
                        0xFFFFFFFF,
                        Offset(
                            -2980 - Constants.OFFSETMAP["russia"]!!.xOffset,
                            5058 + Constants.OFFSETMAP["russia"]!!.yOffset
                        )
                    )
                )
            )
        }
    }
}
