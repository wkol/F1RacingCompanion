package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingService
import com.google.gson.Gson
import javax.inject.Inject

class LiveTimingRepository @Inject constructor(private val webSocketService: LiveTimingService) {
    fun subscribeToData() {
        val data = mapOf(
            "H" to "Streaming",
            "M" to "Subscribe",
            "A" to "[\"Heartbeat\", \"CarData.z\", \"Position.z\",\n" +
                    "\"ExtrapolatedClock\", \"TopThree\", \"RcmSeries\",\n" +
                    "\"TimingStats\", \"TimingAppData\",\n" +
                    "\"WeatherData\", \"TrackStatus\", \"DriverList\",\n" +
                    "\"RaceControlMessages\", \"SessionInfo\",\n" +
                    "\"SessionData\", \"LapCount\", \"TimingData\"]",
            "I" to 1
        )
        webSocketService.subscribeToTopics(Gson().toJson(data))
    }
}