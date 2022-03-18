package com.example.f1racingcompanion.model

import javax.annotation.concurrent.Immutable

@Immutable
data class CircuitOffset(
    val xOffset: Float,
    val yOffset: Float,
    val xAbs: Float,
    val yAbs: Float
)
