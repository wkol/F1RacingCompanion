package com.example.f1racingcompanion.timing

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
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
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.CircuitOffset
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.Tires
import com.example.f1racingcompanion.ui.theme.TitilliumWeb
import com.example.f1racingcompanion.ui.theme.Typography
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
            repeat(3) {
                Box(
                    Modifier
                        .width(30.dp)
                        .height(12.dp)
                        .padding(1.dp)
                        .clip(RoundedCornerShape(60))
                        .background(LiveTimingUtils.getColorFromSector(sectors["$it"]))
                )
            }
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
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Pos",
            modifier = Modifier
                .padding(start = 10.dp, end = 5.dp),
            color = Color.White,
            style = Typography.subtitle2,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "Driver",
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 7.dp),
            color = Color.White,
            style = Typography.subtitle2,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = if (isInterval) "Interval" else "Gap To first",
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1F)
                .clickable { onIntervalChanged() },
            color = Color.White,
            style = Typography.subtitle2,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun TeamColorBox(color: Long, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
                .offset(y = (-3).dp),
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
        driversPosition.forEach { (color, position, isTelemetry) ->
            if (isTelemetry) {
                drawCircle(
                    color = Color.White,
                    radius = 23F,
                    center = Offset(position.x * xScale, position.y * yScale),
                    style = Stroke(width = 3F, cap = StrokeCap.Round)
                )
            }
            drawCircle(
                color = Color(color),
                radius = 20F,
                center = Offset(position.x * xScale, position.y * yScale),
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
        modifier = modifier
            .clickable { onClick() }
            .alpha(if (driverListElement.retired) 0.5F else 1F),
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
        Spacer(Modifier.width(5.dp))
        TeamColorBox(
            color = driverListElement.teamColor,
            modifier = Modifier
                .width(7.dp)
                .fillMaxHeight(0.6F)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = "${driverListElement.firstName[0]}.${driverListElement.lastName}",
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 5.dp),
            color = Color.White,
            style = Typography.subtitle1,
            textAlign = TextAlign.Start
        )
        Text(
            text = if (isInterval) driverListElement.interval else driverListElement.toFirst,
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 5.dp),
            color = Color.White,
            textAlign = TextAlign.End,
            style = Typography.subtitle1,
        )
    }
}

@Composable
fun TelemetryIconButton(modifier: Modifier = Modifier, onTelemetryStart: () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Telemetry", style = Typography.subtitle2)
        Spacer(Modifier.height(2.dp))
        IconButton(onClick = onTelemetryStart) {
            Icon(
                painter = painterResource(id = R.drawable.telemetry_arrow),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun DriverDetails(
    modifier: Modifier = Modifier,
    driver: F1DriverListElement,
    onTelemetryStart: (Int) -> Unit
) {
    Box(modifier = modifier) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround) {
            TiresIndicator(modifier = Modifier.fillMaxHeight(), tires = driver.tires)
            LapTimeIndicator(modifier = Modifier.fillMaxHeight(), lapTime = driver.lastLapTime)
            SectorIndicator(modifier = Modifier.fillMaxHeight(), sectors = driver.lastSectors)
            AnimatedVisibility(visible = LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
                TelemetryIconButton(
                    onTelemetryStart = { onTelemetryStart(driver.carNumber) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StandingLazyList(
    standing: List<F1DriverListElement>,
    fastestLap: FastestRaceLap,
    onTelemetryStart: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isInterval by remember { mutableStateOf(false) }
    val expandedStates = remember(standing.size) {
        standing.map { it.carNumber to false }.toMutableStateMap()
    }

    Column(modifier = modifier) {
        StandingHeader(
            modifier = Modifier
                .height(25.dp)
                .fillMaxWidth()
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
                    expandedStates[element.carNumber] = (expandedStates[element.carNumber])?.not() ?: false
                }
                AnimatedVisibility(expandedStates[element.carNumber] == true) {
                    DriverDetails(
                        driver = element,
                        onTelemetryStart = onTelemetryStart,
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

@Composable
fun TelemtrySection(
    telemetry: () -> Telemetry,
    onEndTelemetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = onEndTelemetry,
                modifier = Modifier
                    .size(15.dp)
                    .padding(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
            Text(
                text = "Telemetry",
                style = Typography.body2,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            )
        }
        TelemetryBoxInfo(
            speed = { telemetry().speed },
            rpm = { telemetry().rpm },
            throttleValue = { telemetry().throttle.value },
            brakeValue = { telemetry().brake.value },
            gearValue = { telemetry().gear },
            isDrsEnabled = { telemetry().isDrs },
            driverStr = { telemetry().driverStr ?: "-" },
            sectors = { telemetry().sectors }
        )
    }
}

@Composable
fun TelemetryBoxInfo(
    speed: () -> Int,
    rpm: () -> Int,
    throttleValue: () -> Float,
    brakeValue: () -> Float,
    gearValue: () -> Int,
    isDrsEnabled: () -> Boolean,
    driverStr: () -> String,
    sectors: () -> Map<String, SectorValue>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = driverStr(),
            style = Typography.subtitle2,
            modifier = Modifier
                .padding(1.dp)
        )
        SectorTimesRow(
            sectorValue = sectors(),
            modifier = Modifier
                .padding(2.dp)
                .height(IntrinsicSize.Min)
        )
        Row(
            modifier = Modifier
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabeledText(
                label = "Speed",
                text = "${speed()}",
                modifier = Modifier
                    .padding(horizontal = 3.dp)
            )
            LabeledText(
                label = "RPM",
                text = "${rpm()}",
                modifier = Modifier
                    .padding(horizontal = 3.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TelemetryValueLine(
                percentage = throttleValue(),
                label = "Throttle",
                color = Color(0xFF024981),
                modifier = Modifier.padding(2.dp)
            )
            TelemetryValueLine(
                percentage = brakeValue(),
                label = "Brakes",
                color = Color(0xFF801414),
                modifier = Modifier.padding(2.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gear",
                modifier = Modifier.padding(4.dp),
                style = Typography.subtitle2,
                color = Color.White
            )
            AnimatedIntValue(
                value = gearValue(),
                modifier = Modifier
                    .padding(2.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            DRSIndicator(
                isDRSenabled = isDrsEnabled(),
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        Color(
                            0xFF5A5959
                        )
                    )
                    .padding(2.dp)
            )
        }
    }
}

@Composable
fun SectorTimesRow(sectorValue: Map<String, SectorValue>, modifier: Modifier) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        repeat(3) {
            LabeledText(
                label = "S${it + 1}",
                text = sectorValue["$it"]?.value ?: "-",
                textStyle = Typography.body2.copy(
                    color = LiveTimingUtils.getColorFromSector(
                        sectorValue[it.toString()]
                    )
                ),
                labelStyle = Typography.body2,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1F)
            )
        }
    }
}

@Composable
fun DRSIndicator(isDRSenabled: Boolean, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = "DRS",
            color = if (isDRSenabled) Color(0xFF26852D) else Color(0xFF919191),
            style = Typography.body2
        )
    }
}

@Composable
fun LabeledText(
    label: String,
    text: String,
    labelStyle: TextStyle = Typography.subtitle2,
    textStyle: TextStyle = Typography.body2,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(1.dp),
            style = labelStyle,
        )
        Text(
            text = text,
            modifier = Modifier.padding(1.dp),
            style = textStyle,
        )
    }
}

@Composable
fun TelemetryValueLine(
    percentage: Float,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(35.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            modifier = Modifier
                .padding(bottom = 2.dp),
            color = Color.White,
            style = Typography.body2,
        )
        Canvas(
            modifier = Modifier
                .height(100.dp)
                .padding(horizontal = 5.dp, vertical = 1.dp)
        ) {
            repeat(10) {
                drawRoundRect(
                    color = if ((it + 1) / 10F < percentage) color else Color.Transparent,
                    topLeft = Offset(size.width / 2F - 15, it.toFloat() * 20),
                    size = Size(30F, 10F),
                    cornerRadius = CornerRadius(5F, 3F),
                )
                drawRoundRect(
                    color = Color(0xFF474747),
                    topLeft = Offset(size.width / 2F - 15, it.toFloat() * 20),
                    size = Size(30F, 10F),
                    cornerRadius = CornerRadius(5F, 3F),
                    style = Stroke(2F)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedIntValue(value: Int, modifier: Modifier = Modifier) {
    var lastValue by remember { mutableStateOf(value) }
    val color =
        remember(value) { Animatable(if (value < lastValue) Color(0xFFB71C1C) else Color(0xFF26852D)) }
    LaunchedEffect(key1 = value) {
        color.animateTo(Color.White, animationSpec = tween(durationMillis = 500))
        lastValue = value
    }
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            intChangeAnimation().using(
                SizeTransform(clip = false)
            )
        },
        modifier = modifier
    ) {
        Text(
            text = "$value",
            modifier = Modifier
                .padding(horizontal = 1.dp),
            color = color.value,
            style = Typography.subtitle2,
            textAlign = TextAlign.Start
        )
    }
}

@ExperimentalAnimationApi
private fun intChangeAnimation(duration: Int = 250): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> 2 * height / 3 } +
        fadeIn(
            animationSpec = tween(durationMillis = duration)
        ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -2 * height / 3 } +
        fadeOut(
            animationSpec = tween(durationMillis = duration)
        )
}
