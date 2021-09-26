package com.example.f1racingcompanion.utils

object Constants {
    val CHANNELS_MAP = hashMapOf(
        "0" to "RPM",
        "2" to "Speed",
        "3" to "nGear",
        "4" to "Throttle",
        "5" to "Brake",
        "45" to "DRS"
    )
    const val HUB_NAME = "Streaming"

    const val HUB_DATA = "[\"name\": \"Streaming\"]"

    const val LIVETIMING_NEGOTIATE_URL = "https://livetiming.formula1.com/signalr/"

    const val WEBSCOKET_HOST = "livetiming.formula1.com"

    const val WEBSOCKET_SIGNALR_PATH = "signalr"

    const val WEBSOCKET_CONNECT_PATH = "connect"

    const val WEBSCOKET_TRANSPORT = "webSockets"

    const val WEBSCOKET_PROTOCOL = "1.5"

    val SUBSRIBED_STREAMS = listOf(
        "Heartbeat", "CarData.z", "Position.z",
        "ExtrapolatedClock", "TopThree", "RcmSeries",
        "TimingStats", "TimingAppData",
        "WeatherData", "TrackStatus", "DriverList",
        "RaceControlMessages", "SessionInfo",
        "SessionData", "LapCount", "TimingData"
    )
}

