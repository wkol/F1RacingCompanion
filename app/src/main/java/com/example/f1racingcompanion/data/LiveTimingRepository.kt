package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.utils.Constants
import com.tinder.scarlet.websocket.WebSocketEvent
import kotlinx.coroutines.flow.*
import timber.log.Timber

class LiveTimingRepository(private val webSocketService: LiveTimingService) {

    fun startWebSocket() = flow {
        webSocketService.observeEvents().collect { event ->
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
            emit(event)
            Timber.d("Received event: ${event::class.java.simpleName}")
        }
    }

    fun getTimingStats() = flow {
        webSocketService.observeTimingStats().collect { data ->
            Timber.d("Received TimingStatsData")
            data.forEach {
                emit(it)
            }
        }
    }

    fun getTimingData() = flow {
        webSocketService.observeTimingData().collect { data ->
            Timber.d("Received TimingData")
            data.forEach {
                emit(it)
            }
        }
    }

    fun getDriverTelemetry(number: Int) = flow {
        webSocketService.observeTelemetry().collect { data ->
            Timber.d("Received CarData")
            data.forEach {
                for (entry in it.data!!.entries) {
                    emit(entry[number])
                }
            }
        }
    }

    fun getPositions() = flow {
        webSocketService.observeCarPosition().collect { data ->
            Timber.d("Received PositionData")
            data.forEach {
                emit(it)
            }
        }
    }

}
