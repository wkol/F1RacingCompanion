package com.example.f1racingcompanion.ui.timings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1Driver
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.Tires
import com.example.f1racingcompanion.ui.FormulaTextField
import com.example.f1racingcompanion.ui.theme.TitilliumWeb
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.LiveTimingUtils
import kotlin.math.absoluteValue

@Composable
fun DriverElement(
    modifier: Modifier = Modifier,
    driverListElement: F1DriverListElement,
    isInterval: Boolean = remember { true }
) {
    Row(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x111E1E1E)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            Modifier
                .padding(start = 3.dp, top = 3.dp, end = 3.dp, bottom = 3.dp)
                .size(20.dp)
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
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight(0.9f)
                .width(8.dp)
                .background(
                    Color(driverListElement.teamColor.colorRGB),
                    RoundedCornerShape(5)
                )
                .padding(horizontal = 10.dp)
        )
        FormulaTextField(
            text = "${driverListElement.firstName[0]}.${driverListElement.lastName}",
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentSize(Alignment.CenterStart)
                .padding(horizontal = 10.dp),
            color = Color.White
        )
        FormulaTextField(
            text = if (isInterval) driverListElement.interval else driverListElement.toFirst,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterEnd)
                .padding(horizontal = 10.dp),
            color = Color.White
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
fun DriverPlot(modifier: Modifier = Modifier, driversPosition: SnapshotStateMap<F1Driver, Offset>) {

    val offset = Constants.OFFSETMAP["russia"]!!
    Canvas(modifier) {
        val xScale: Float = size.width / (offset[1] - offset[0]).absoluteValue
        val yScale: Float = size.height / (offset[3] - offset[2]).absoluteValue
        driversPosition.forEach { (f1Driver, position) ->
            val color = f1Driver.teamColor
            drawCircle(
                color = Color(color.colorRGB),
                radius = 20F,
                center = Offset(position.x * xScale, position.y * yScale)
            )

        }
    }
}

@Composable
fun SectorIndicator(
    modifier: Modifier = Modifier,
    sectors: Map<String, SectorValue> = remember { emptyMap() }
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
            Box(
                Modifier
                    .width(30.dp)
                    .height(12.dp)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(60))
                    .background(LiveTimingUtils.getColorFromSector(sectors["3"]))
            )
        }
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
fun DriverDetails(modifier: Modifier = Modifier, driver: F1DriverListElement) {
    Box(
        modifier
            .clip(
                RoundedCornerShape(
                    topStartPercent = 0,
                    topEndPercent = 0,
                    bottomEndPercent = 20,
                    bottomStartPercent = 20
                )
            )
            .background(Color(0xFF3F4144))
    ) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround) {
            TiresIndicator(modifier = Modifier.fillMaxHeight(), tires = driver.tires)
            LapTimeIndicator(modifier = Modifier.fillMaxHeight(), lapTime = driver.lapTime)
            SectorIndicator(modifier = Modifier.fillMaxHeight(), sectors = driver.lastSectors)
        }
    }

}

@Composable
fun StandingHeader(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            FormulaTextField(
                text = "Pos", modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.13f)
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 10.dp),
                color = Color.White, fontSize = 15
            )
            Spacer(modifier = Modifier.width(20.dp))
            FormulaTextField(
                text = "Driver", modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.CenterStart)
                    .padding(horizontal = 10.dp),
                color = Color.White, fontSize = 15
            )
            Spacer(modifier = Modifier.width(20.dp))
            FormulaTextField(
                text = "Gap/Interval", modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterEnd)
                    .padding(horizontal = 10.dp),
                color = Color.White, fontSize = 15
            )
        }
    }
}

@Preview("TimingScreenPreview")
@Composable
fun DriverElementPreview() {
    var isExpanded by remember { mutableStateOf(true) }
    val element = F1DriverListElement(
        "1:22:11",
        mutableMapOf(
            "1" to SectorValue(
                "13.78",
                personalFastest = false,
                overallFastest = true,
                null
            ),
            "2" to SectorValue(
                "13.78",
                personalFastest = false,
                overallFastest = false,
                null
            ),
            "3" to SectorValue(
                "13.78",
                personalFastest = true,
                overallFastest = false,
                null
            )
        ),
        Tires(Compound.HARD, false, 2),
        1,
        "+1,4",
        "+9.0",
        false,
        33
    )
    val x = remember {
        mutableStateMapOf(
            F1Driver.getDriverByNumber(33) to Offset(
                -12930.6F - Constants.OFFSETMAP["russia"]!![0],
                Constants.OFFSETMAP["russia"]!![3] + 2094.0F
            )
        )
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        StandingHeader(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.03f)
                .background(Color.Gray)
        )
        DriverElement(
            modifier = Modifier.height(40.dp).clickable { isExpanded = !isExpanded },
            driverListElement = element,
            isInterval = false
        )
        AnimatedVisibility(visible = isExpanded) {
            DriverDetails(driver = element, modifier = Modifier.height(60.dp))
        }
        Spacer(Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3F)
                .clip(RoundedCornerShape(80F))
                .border(BorderStroke(2.dp, Color(0xFF474747)), RoundedCornerShape(80F))
                .background(Color(0xFF393E46))
        ) {
            CircuitPlot(
                modifier = Modifier
                    .fillMaxSize(0.95F)
                    .align(Alignment.Center), circuitName = "russia"
            )
            DriverPlot(
                modifier = Modifier
                    .fillMaxSize(0.95F)
                    .align(Alignment.Center), driversPosition = x
            )
        }
//        x[F1Driver.getDriverByNumber(33)] = Offset(-12830.6F - Constants.offsetMap["russia"]!![0], Constants.offsetMap["russia"]!![3]+2094.0F)
    }
}