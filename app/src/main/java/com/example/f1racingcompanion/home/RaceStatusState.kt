package com.example.f1racingcompanion.home

data class RaceStatusState(
    val isActive: Boolean? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)
