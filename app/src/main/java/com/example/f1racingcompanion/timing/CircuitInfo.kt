package com.example.f1racingcompanion.timing

import androidx.annotation.DrawableRes
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.model.CircuitOffset

data class CircuitInfo(
    val circuitOffset: CircuitOffset,
    val grandPrixName: String,
    @DrawableRes
    val circuitMap: Int
) {
    companion object {
        fun getUnknownCircuitInfo(): CircuitInfo {
            return CircuitInfo(CircuitOffset(0.0F, 0.0F, 0.0F, 0.0F), "Unknown", R.drawable.minutes_tires)
        }
    }
}
