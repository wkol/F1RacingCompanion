package com.example.f1racingcompanion.home

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.model.NextSession
import com.example.f1racingcompanion.ui.theme.TitilliumWeb

@Composable
fun DateTextInsideTire(value: Long, label: String, imageID: Int, modifier: Modifier = Modifier) {
    val painter = painterResource(id = imageID)
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(100.dp)) {
            with(painter) {
                draw(size)
            }
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    value.toString(),
                    size.width / 2f,
                    size.height / 1.65f,
                    Paint().apply {
                        color = 0xFFFFFFFF.toInt()
                        textSize = size.height / 3f
                        textAlign = Paint.Align.CENTER
                        typeface = Typeface.DEFAULT_BOLD
                    }
                )
            }
        }
        Text(
            "$value $label",
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun ActiveStatusText(activeStatus: RaceStatusState, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = "Session status is " + if (activeStatus.isActive == false) "offline" else if (activeStatus.isActive == true) "online" else "",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
    }
}

@Composable
fun FetchingDataIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Fetching data...",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
            )
        }
    }
}

@Composable
fun NetworkError(modifier: Modifier, errorMsg: String) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "An error while fetching data occured: $errorMsg",
            style = TextStyle(
                fontFamily = TitilliumWeb,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = Color.Black
            ),
            modifier = Modifier.wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SessionDate(countdown: MeetingCountdown, sessionName: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0x72000000))
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .wrapContentSize(Alignment.Center),
                text = "$sessionName starts in: ",
                fontFamily = TitilliumWeb,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            DateTextInsideTire(
                value = countdown.days,
                label = if (countdown.days != 1L) "days" else "day",
                imageID = R.drawable.days_tires,
                modifier = Modifier.padding(20.dp)
            )
            DateTextInsideTire(
                value = countdown.hours,
                label = if (countdown.hours != 1L) "hours" else "hour",
                imageID = R.drawable.hours_tires,
                modifier = Modifier.padding(20.dp)
            )
            DateTextInsideTire(
                value = countdown.minutes,
                label = if (countdown.minutes != 1L) "minutes" else "minutes",
                imageID = R.drawable.minutes_tires,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun SessionInfo(nextSession: NextSession, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(5, 5, 0, 0))
            .background(Color(0x57141330))
    ) {
        Column(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.97F)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Next session:",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopCenter),
                fontSize = 15.sp,
                color = Color.White
            )
            Text(
                text = nextSession.raceName,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}
