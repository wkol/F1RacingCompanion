package com.example.f1racingcompanion.ui

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Timing : Screen("timing_screen")

    fun withArgs(vararg args: String) = buildString {
        append(route)
        args.forEach { append("/$it") }
    }
}
