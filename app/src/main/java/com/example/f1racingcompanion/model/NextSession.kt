package com.example.f1racingcompanion.model

data class NextSession(
    val circuitName: String,
    val circuitcountry: String,
    val schedule: List<RaceScheduleItem>,
)
