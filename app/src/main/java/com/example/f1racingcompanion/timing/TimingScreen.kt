package com.example.f1racingcompanion.timing

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.ui.FetchingDataIndicator
import com.example.f1racingcompanion.ui.theme.F1RacingCompanionTheme

@Composable
fun CircuitDriverPlot(
    circuitInfo: CircuitInfo,
    driversPostitions: List<Position>,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        CircuitPlot(
            circuitMapId = circuitInfo.circuitMap,
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
fun TimingContent(
    circuitInfo: CircuitInfo,
    standing: List<F1DriverListElement>,
    fastestLap: FastestRaceLap,
    positions: List<Position>,
    sessionType: String,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (isLoading) {
            FetchingDataIndicator(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                text = "Connecting to API..."
            )
        } else {
            RaceNameText(
                raceName = circuitInfo.grandPrixName,
                modifier = Modifier.align(Alignment.Start),
                sessionType = sessionType
            )
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Row(modifier = Modifier.fillMaxSize()) {
                    StandingLazyList(
                        standing = standing.sortedBy { it.position },
                        fastestLap = fastestLap,
                        modifier = Modifier
                            .fillMaxWidth(0.5F)
                            .fillMaxHeight()
                    )
                    Spacer(Modifier.height(10.dp))
                    CircuitDriverPlot(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(80F))
                            .border(BorderStroke(2.dp, Color(0xFF474747)), RoundedCornerShape(80F))
                            .background(Color(0x57141330)),
                        circuitInfo = circuitInfo,
                        driversPostitions = positions
                    )
                }
            }
            StandingLazyList(
                standing = standing.sortedBy { it.position },
                fastestLap = fastestLap,
                modifier = Modifier
                    .fillMaxWidth(0.95F)
                    .fillMaxHeight(0.65F)
            )
            Spacer(Modifier.height(10.dp))
            CircuitDriverPlot(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(80F))
                    .border(BorderStroke(2.dp, Color(0xFF474747)), RoundedCornerShape(80F))
                    .background(Color(0x57141330)),
                circuitInfo = circuitInfo,
                driversPostitions = positions
            )
        }
    }
}

@Composable
fun TimingScreen(timingViewModel: TimingViewModel = viewModel()) {
    val standing by timingViewModel.standing.collectAsState()
    val positions by timingViewModel.driversPosition.collectAsState()
    val fastestLap by timingViewModel.fastestLap.collectAsState()
    val isLoading by timingViewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = Unit) {
        timingViewModel.refreshWebSocket()
    }
    Scaffold(modifier = Modifier.fillMaxSize()) {
        TimingContent(
            circuitInfo = timingViewModel.circuitInfo,
            standing = standing,
            fastestLap = fastestLap,
            positions = positions,
            isLoading = isLoading,
            sessionType = timingViewModel.sessionType
        )
    }
}

@Preview("TimingScreenPreview")
@Composable
fun TimingScreenPreview(@PreviewParameter(SampleTimingDataProvider::class) data: TimingPreviewData) {
    F1RacingCompanionTheme(darkTheme = true) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            TimingContent(
                circuitInfo = data.circuitInfo,
                standing = data.standing,
                fastestLap = data.fastestLap,
                positions = data.driversPositions,
                isLoading = data.isLoading,
                sessionType = data.sessionType
            )
        }
    }
}
