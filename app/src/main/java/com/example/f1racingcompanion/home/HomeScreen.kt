package com.example.f1racingcompanion.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.f1racingcompanion.ui.FetchingDataIndicator
import com.example.f1racingcompanion.ui.NetworkError
import com.example.f1racingcompanion.ui.Screen
import com.example.f1racingcompanion.ui.theme.F1RacingCompanionTheme

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel(), navController: NavController) {
    val uiState by homeViewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    F1RacingCompanionTheme(darkTheme = true) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "F1 Racing Companion",
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            color = Color.White
                        )
                    },
                    backgroundColor = Color(0x72000000)
                )
            },
        ) { paddingValue ->
            HomeContent(
                uiState,
                Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
            ) {
                navController.navigate(Screen.Timing.withArgs(uiState.nextSession?.circuitId ?: "unknown"))
            }
        }
    }
}

@Composable
fun HomeContent(uiState: RaceStatusState, modifier: Modifier = Modifier, onActiveSession: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            FetchingDataIndicator(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
            )
        } else {
            when (uiState.isActive) {
                false -> {
                    SessionInfo(
                        nextSession = uiState.nextSession!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                    SessionDate(
                        countdown = uiState.countdown!!,
                        sessionName = uiState.nextSession.schedule[0].description,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    )
                }
                true -> {
                    Button(
                        modifier = Modifier.size(100.dp),
                        onClick = { onActiveSession() }
                    ) {
                        Text(
                            text = "CONTINUE TO LIVE",
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
                null -> {
                    NetworkError(
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFFFFF)),
                        uiState.error
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(@PreviewParameter(SampleRaceStatusStateDataProvider::class) raceStatusState: RaceStatusState) {
    F1RacingCompanionTheme(darkTheme = true) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "F1 Racing Companion",
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            color = Color.White
                        )
                    },
                    backgroundColor = Color(0x72000000)
                )
            },
        ) { paddingValue ->
            HomeContent(
                raceStatusState,
                Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {}
        }
    }
}
