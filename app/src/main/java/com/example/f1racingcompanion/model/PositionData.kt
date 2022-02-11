package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto

data class PositionData(
    val timeStamp: String,
    val position: List<PositionOnTrack>
)


