package com.example.f1racingcompanion.timing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.CircuitOffset
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.Tires
import com.example.f1racingcompanion.ui.theme.TitilliumWeb
import com.example.f1racingcompanion.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.random.Random

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
        Box(
            Modifier
                .padding(start = 8.dp, top = 3.dp, end = 3.dp, bottom = 3.dp)
                .size(25.dp)
                .clip(RoundedCornerShape(10))
                .background(Color.White)
        ) {
            Text(
                text = driverListElement.position.toString(),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopCenter)
                    .offset(y = (-2).dp),
                fontFamily = TitilliumWeb,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight(0.9f)
                .width(8.dp)
                .background(
                    Color(driverListElement.teamColor),
                    RoundedCornerShape(5)
                )
                .padding(horizontal = 10.dp)
        )
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
fun CircuitPlot(modifier: Modifier = Modifier, circuitName: String) {
    Constants.CIRCIUTS[circuitName]?.let {
        val painter = painterResource(id = it)
        Canvas(modifier = modifier) {
            with(painter) {
                draw(size)
            }
        }
    }
}

@Composable
fun DriverPlot(modifier: Modifier = Modifier, driversPosition: State<Map<Int, Offset>>, offset: CircuitOffset) {
    Canvas(modifier) {
        val xScale: Float = size.width / offset.xAbs
        val yScale: Float = size.height / offset.yAbs
        driversPosition.value.forEach { (f1Driver, position) ->
            val color = 0x111111
            drawCircle(
                color = Color(color),
                radius = 20F,
                center = Offset(position.x * xScale, position.y * yScale)
            )
        }
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
    var isInterval by remember { mutableStateOf(true) }
    Column(modifier = Modifier.fillMaxSize()) {
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
        val expandedStates = remember(viewModel.standing) {
            viewModel.standing.value.map { false }.toMutableStateList()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.8F)
                .padding(2.dp)
        ) {
            viewModel.standing.value.forEachIndexed { idx, element ->
                val expandedState = expandedStates[idx]
                item(key = element.carNumber) {
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
                            .background(Color(0x60151735)),
                        driverListElement = element,
                        isInterval = isInterval
                    ) {
                        expandedStates[idx] = !expandedState
                    }
                    AnimatedVisibility(expandedState) {
                        DriverDetails(
                            driver = element,
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth(),
                            color = Color(0x57141330)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3F)
                .clip(RoundedCornerShape(80F))
                .border(BorderStroke(2.dp, Color(0xFF474747)), RoundedCornerShape(80F))
                .background(Color(0x57141330))
        ) {
            CircuitPlot(
                modifier = Modifier
                    .fillMaxSize(0.95F)
                    .align(Alignment.Center),
                circuitName = "russia"
            )
            DriverPlot(
                modifier = Modifier
                    .fillMaxSize(0.95F)
                    .align(Alignment.Center),
                driversPosition = viewModel.driversPosition.collectAsState(),
                offset = viewModel.offsets
            )
        }
    }
}

@Composable
fun StandingLazyList(
    standing: SnapshotStateList<F1DriverListElement>,
    fastestLap: FastestRaceLap,
) {
    var isInterval by remember { mutableStateOf(false) }
    val expandedStates = remember(standing) {
        standing.map { false }.toMutableStateList()
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
            .fillMaxWidth()
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
                        .fillMaxWidth(),
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
                    ),
                    "3" to SectorValue(
                        "13.78",
                        personalFastest = true,
                        overallFastest = false,
                        previousValue = null,
                        segments = null
                    )
                ),
                Tires(Compound.HARD, false, 2),
                1,
                "+1,4",
                "+9.0",
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
    Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
        Column(modifier = Modifier.fillMaxSize()) {
            RaceNameText(raceName = "Russia")
            StandingLazyList(standing = elements, fastestLap = FastestRaceLap(1, "1:11"))
            Spacer(Modifier.height(10.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(80F))
                    .border(BorderStroke(2.dp, Color(0xFF474747)), RoundedCornerShape(80F))
                    .background(Color(0x57141330))
                    .clickable {
                        elements[0].toFirst = "${Random.nextInt(0, 10)}"
                        elements[0] = elements[0].copy()
                    }
            ) {
                CircuitPlot(
                    modifier = Modifier
                        .fillMaxSize(0.9F)
                        .align(Alignment.Center),
                    circuitName = "russia"
                )
                DriverPlot(
                    modifier = Modifier
                        .fillMaxSize(0.9F)
                        .align(Alignment.Center),
                    driversPosition = MutableSharedFlow<Map<Int, Offset>>().collectAsState(emptyMap()),
                    offset = Constants.OFFSETMAP["russia"]!!
                )
            }
        }
    }
}
