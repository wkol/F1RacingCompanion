package com.example.f1racingcompanion.data.nextsessiondto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RaceInfoDto(
    val meetingCountryName: String,
    val meetingOfficialName: String
)
