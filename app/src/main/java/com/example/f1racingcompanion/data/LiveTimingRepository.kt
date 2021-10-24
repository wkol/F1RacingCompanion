package com.example.f1racingcompanion.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
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
            emit(data)
        }
    }

    fun getTimingData() = flow {
        webSocketService.observeTimingData().collect { data ->
            emit(data)
            Timber.d("Received TimingData")
//            data.forEach {
//                emit(it)
//            }
//        }
        }
    }

    fun getDriverTelemetry(number: Int) = flow {
        webSocketService.observeTelemetry().collect { data ->
            Timber.d("Received CarData")
            for (entry in data.data!!.entries) {
                emit(entry[number])
            }
        }
    }

    fun getPositions() = flow {
        webSocketService.observeCarPosition().collect { data ->
            Timber.d("Received PositionData")
            emit(data)
        }
    }

}

