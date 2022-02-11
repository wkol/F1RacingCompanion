package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.utils.Constants
import com.tinder.scarlet.websocket.WebSocketEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class LiveTimingRepository(private val webSocketService: LiveTimingService) {

    fun subscribe()  {
        webSocketService.subscribeToTopics(Subscribe(
            "Streaming",
            "Subscribe",
            listOf(Constants.SUBSRIBED_STREAMS),
            0
        ))
    }

    fun startWebSocket() =
        webSocketService.observeEvents().onEach { event ->
            if (event is WebSocketEvent.OnConnectionOpened) {
                webSocketService.subscribeToTopics(
                    Subscribe(
                        "Streaming",
                        "Subscribe",
                        listOf(Constants.SUBSRIBED_STREAMS),
                        0
                    )
                )
                Timber.d("Sent subscribe request")
            }
            if (event is WebSocketEvent.OnMessageReceived) {
                Timber.d(event.message.toString())
            }
            Timber.d("Received event: ${event::class.java.simpleName}")
        }.flowOn(Dispatchers.IO)

    fun getTimingStats() = webSocketService.observeTimingStats().onEach {
        Timber.d("Received TimingStatsData")
    }.flowOn(Dispatchers.IO)

    fun getTimingData() = webSocketService.observeTimingData().onEach {
        Timber.d("Received TimingData")
    }
        .flowOn(Dispatchers.IO)

    fun getDriverTelemetry(number: Int) = flow {
        webSocketService.observeTelemetry().flowOn(Dispatchers.IO).collect { data ->
            Timber.d("Received CarData")
            for (entry in data.data!!.entries) {
                emit(entry.cars[number])
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getPositions() = webSocketService.observeCarPosition().onEach {
        Timber.d("Received PositionData")
    }.flowOn(Dispatchers.IO)

}

