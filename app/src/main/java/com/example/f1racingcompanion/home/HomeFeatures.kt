package com.example.f1racingcompanion.home

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.model.NextSession
import com.example.f1racingcompanion.model.RaceScheduleItem
import com.example.f1racingcompanion.timing.CircuitInfo
import com.example.f1racingcompanion.utils.Constants
import java.time.ZonedDateTime

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
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TiresCountdown(countdown = countdown)
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TiresCountdown(countdown = countdown)
                }
            }
        }
    }
}

@Composable
fun TiresCountdown(countdown: MeetingCountdown) {
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

@Composable
fun ActiveSessionInfo(
    sessionInfo: NextSession,
    circuitInfo: CircuitInfo,
    timeElapsed: MeetingCountdown,
    onLiveButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(0, 0, 10, 10))
            .background(Color(0x57141330))
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
            Row(
                modifier = Modifier.fillMaxHeight(0.86F),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = circuitInfo.circuitMap),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .padding(5.dp)
                        .scale(0.8F)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Live session:",
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = sessionInfo.raceName,
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "${sessionInfo.schedule.firstOrNull()?.description ?: ""} T+" + "%02d:%02d".format(
                            timeElapsed.hours,
                            timeElapsed.minutes
                        ),
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLiveButtonClicked() }
                    .background(Color(0xFFB71C1C))
            ) {
                Text(
                    text = "Go Live",
                    modifier = Modifier
                        .fillMaxSize(),
                    textAlign = TextAlign.Center,

                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(name = "Session Info")
@Composable
fun SessionInfoPreview() {
    val nextSession = NextSession(
        circuitName = "circuitName",
        raceName = "Very very very long long long long long long circuit name",
        schedule = listOf(
            RaceScheduleItem(
                isUpcoming = true,
                description = "Qualifying",
                zonedStartTime = ZonedDateTime.now()
            )
        ),
        circuitId = "1"
    )
    SessionInfo(
        nextSession = nextSession,
        modifier = Modifier.size(width = 500.dp, height = 200.dp)
    )
}

@Preview(name = "Active Session Info")
@Composable
fun ActiveSessionPreview() {
    val activeSession = NextSession(
        circuitName = "circuitName",
        raceName = "Very very very long long long long long long circuit name",
        schedule = listOf(
            RaceScheduleItem(
                isUpcoming = true,
                description = "Qualifying",
                zonedStartTime = ZonedDateTime.now()
            )
        ),
        circuitId = "1"
    )

    ActiveSessionInfo(
        sessionInfo = activeSession,
        circuitInfo = Constants.CIRCUITS["imola"]!!,
        timeElapsed = MeetingCountdown(days = 0, hours = 2, minutes = 59),
        onLiveButtonClicked = {},
        modifier = Modifier.size(width = 500.dp, height = 200.dp)
    )
}
