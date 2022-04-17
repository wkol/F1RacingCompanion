package com.example.f1racingcompanion.model

data class NextSession(
    val circuitId: String,
    val circuitName: String,
    val raceName: String,
    val schedule: List<RaceScheduleItem>,
)
