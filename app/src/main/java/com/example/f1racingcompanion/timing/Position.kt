package com.example.f1racingcompanion.timing

import androidx.compose.ui.geometry.Offset

data class Position(
    val color: Long,
    val offset: Offset,
    val isTelemetry: Boolean = false
)
