package com.example.f1racingcompanion.timing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.Tires
import com.example.f1racingcompanion.ui.theme.TitilliumWeb
import com.example.f1racingcompanion.utils.LiveTimingUtils

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
fun RaceNameText(modifier: Modifier = Modifier, raceName: String) {
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
            text = " GRAND PRIX", color = Color(0xBFFFFFFF), fontFamily = TitilliumWeb,
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
                    .fillMaxWidth(0.13f)
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 10.dp),
                color = Color.White, fontSize = 15.sp,
                fontFamily = TitilliumWeb,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Driver",
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.CenterStart)
                    .padding(horizontal = 10.dp),
                color = Color.White, fontSize = 15.sp,
                fontFamily = TitilliumWeb,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = if (isInterval) "Interval" else "Gap To first",
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterEnd)
                    .padding(horizontal = 10.dp)
                    .clickable { onIntervalChanged() },
                color = Color.White,
                fontSize = 15.sp,
                fontFamily = TitilliumWeb,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
