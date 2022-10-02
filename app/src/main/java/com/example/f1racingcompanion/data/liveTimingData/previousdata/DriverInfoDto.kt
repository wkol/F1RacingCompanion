package com.example.f1racingcompanion.data.liveTimingData.previousdata

data class DriverInfoDto(
    var broadcastName: String,
    var countryCode: String,
    var firstName: String,
    var fullName: String,
    var lastName: String,
    var racingNumber: Int,
    var reference: String,
    var teamColour: String,
    var teamName: String,
    var tla: String
)
