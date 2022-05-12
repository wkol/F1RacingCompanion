package com.example.f1racingcompanion.timing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.CircuitOffset
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.Tires
import com.example.f1racingcompanion.ui.theme.TitilliumWeb
import com.example.f1racingcompanion.utils.LiveTimingUtils

@Composable
fun SectorIndicator(
    modifier: Modifier = Modifier,
    sectors: Map<String, SectorValue>
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sectors",
            color = Color.White,
            fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(
                Modifier
                    .width(30.dp)
                    .height(12.dp)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(60))
                    .background(LiveTimingUtils.getColorFromSector(sectors["0"]))
            )
            Box(
                Modifier
                    .width(30.dp)
                    .height(12.dp)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(60))
                    .background(LiveTimingUtils.getColorFromSector(sectors["1"]))
            )
            Box(
                Modifier
                    .width(30.dp)
                    .height(12.dp)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(60))
                    .background(LiveTimingUtils.getColorFromSector(sectors["2"]))
            )
        }
    }
}

@Composable
fun RaceNameText(modifier: Modifier = Modifier, raceName: String, sessionType: String) {
    Row(modifier, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(8.dp)
                .height(32.dp)
                .background(Color(0xFFB71C1C))
                .clip(RectangleShape)
                .padding(horizontal = 10.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = raceName, color = Color.White, fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
        Text(
            modifier = Modifier.align(Alignment.Bottom),
            text = " GRAND PRIX - ", color = Color(0xBFFFFFFF), fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        Text(
            modifier = Modifier.align(Alignment.Bottom),
            text = sessionType, color = Color(0xBFFFFFFF), fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
    }
}

@Composable
fun TiresIndicator(
    modifier: Modifier = Modifier,
    tires: Tires = remember { Tires(Compound.UNKNOWN, false, 0) }
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Tires",
            color = Color.White,
            fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(Modifier.height(2.dp))
        Image(
            modifier = Modifier
                .fillMaxHeight(0.5F),
            imageVector = ImageVector.vectorResource(
                LiveTimingUtils.getTiresIcon(
                    tires.currentCompound ?: Compound.UNKNOWN
                )
            ),
            contentDescription = tires.currentCompound?.tyreName ?: "Unknown tires"
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "Laps: " + tires.tyreAge.toString(),
            modifier = Modifier.wrapContentSize(),
            color = Color.White,
            fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

@Composable
fun LapTimeIndicator(modifier: Modifier = Modifier, lapTime: String = remember { "" }) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Last lap",
            color = Color.White,
            fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            lapTime,
            color = Color.White,
            fontFamily = TitilliumWeb,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun StandingHeader(
    modifier: Modifier = Modifier,
    isInterval: Boolean,
    onIntervalChanged: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Pos",
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp, end = 5.dp),
                color = Color.White, fontSize = 15.sp,
                fontFamily = TitilliumWeb,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Driver",
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 7.dp),
                color = Color.White, fontSize = 15.sp,
                fontFamily = TitilliumWeb,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = if (isInterval) "Interval" else "Gap To first",
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clickable { onIntervalChanged() },
                color = Color.White,
                fontSize = 15.sp,
                fontFamily = TitilliumWeb,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun TeamColorBox(color: Long) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .fillMaxHeight(0.6f)
            .width(8.dp)
            .background(
                Color(color),
                RoundedCornerShape(5)
            )
            .padding(horizontal = 10.dp)
    )
}

@Composable
fun PositionBox(position: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(RoundedCornerShape(10))
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = position.toString(),
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-2).dp),
            fontFamily = TitilliumWeb,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CircuitPlot(modifier: Modifier = Modifier, circuitMapId: Int) {
    val painter = painterResource(id = circuitMapId)
    Canvas(modifier = modifier) {
        with(painter) {
            draw(size)
        }
    }
}

@Composable
fun DriverPlot(
    modifier: Modifier = Modifier,
    driversPosition: List<Position>,
    offset: CircuitOffset
) {
    Canvas(modifier) {
        val xScale: Float = size.width / offset.xAbs
        val yScale: Float = size.height / offset.yAbs
        driversPosition.forEach { (color, position) ->
            drawCircle(
                color = Color(color),
                radius = 20F,
                center = Offset(position.x * xScale, position.y * yScale)
            )
        }
    }
}

@Composable
fun DriverElement(
    modifier: Modifier = Modifier,
    driverListElement: F1DriverListElement,
    isInterval: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable { onClick() }.alpha(if (driverListElement.retired) 0.5F else 1F),
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
                .padding(horizontal = 10.dp),
            color = Color.White,
            fontSize = 25.sp,
            fontFamily = TitilliumWeb,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
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
fun DriverDetails(modifier: Modifier = Modifier, driver: F1DriverListElement) {
    Box(modifier = modifier) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround) {
            TiresIndicator(modifier = Modifier.fillMaxHeight(), tires = driver.tires)
            LapTimeIndicator(modifier = Modifier.fillMaxHeight(), lapTime = driver.lastLapTime)
            SectorIndicator(modifier = Modifier.fillMaxHeight(), sectors = driver.lastSectors)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StandingLazyList(
    standing: List<F1DriverListElement>,
    fastestLap: FastestRaceLap,
    modifier: Modifier = Modifier
) {
    var isInterval by remember { mutableStateOf(false) }
    val expandedStates = remember {
        List(20) { false }.toMutableStateList()
    }

    Column(modifier = modifier) {
        StandingHeader(
            modifier = Modifier
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
                .padding(2.dp),
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
                        .background(Color(0x60151735))
                        .animateItemPlacement(),
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
                            .padding(2.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 0,
                                    topEndPercent = 0,
                                    bottomEndPercent = 20,
                                    bottomStartPercent = 0
                                )
                            )
                            .background(Color(0x60151735))
                    )
                }
            }
        }
    }
}
