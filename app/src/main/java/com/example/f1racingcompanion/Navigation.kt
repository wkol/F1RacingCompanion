package com.example.f1racingcompanion

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.f1racingcompanion.home.HomeScreen
import com.example.f1racingcompanion.home.HomeViewModel
import com.example.f1racingcompanion.timing.TimingScreen
import com.example.f1racingcompanion.timing.TimingViewModel
import com.example.f1racingcompanion.ui.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(homeViewModel = homeViewModel, navController = navController)
        }
        composable(
            route = Screen.Timing.route + "/{circuit_id}",
            arguments = listOf(
                navArgument("circuit_id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            val timingViewModel = hiltViewModel<TimingViewModel>()
            TimingScreen(timingViewModel = timingViewModel)
        }
    }
}
