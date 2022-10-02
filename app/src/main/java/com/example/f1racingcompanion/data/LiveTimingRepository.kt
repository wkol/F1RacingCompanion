package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.utils.Constants
import com.tinder.scarlet.websocket.WebSocketEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class LiveTimingRepository(private val webSocketService: LiveTimingService) {

    private var _isOpen: Boolean = false
    val isOpen: Boolean
        get() = _isOpen

    fun subscribe() {
        webSocketService.subscribeToTopics(
            Subscribe(
                "Streaming",
                "Subscribe",
                listOf(Constants.SUBSRIBED_STREAMS),
                0
            )
        )
        Timber.d("Subscribed to topics: ${Constants.SUBSRIBED_STREAMS}")
    }

    fun startWebSocket() =
        webSocketService.observeEvents().onEach { event ->
            when (event) {
                is WebSocketEvent.OnConnectionOpened -> {
                    subscribe()
                    _isOpen = true
                }
                is WebSocketEvent.OnConnectionFailed -> _isOpen = false
                is WebSocketEvent.OnConnectionClosing -> _isOpen = false
                else -> {}
            }
            Timber.d("Received event: ${event::class.java.simpleName}")
        }.flowOn(Dispatchers.IO)

    fun getTimingStats() = webSocketService.observeTimingStats().onEach {
        Timber.d("Received TimingStats")
    }.flowOn(Dispatchers.IO)

    fun getTimingAppData() = webSocketService.observeTimingAppData().onEach {
        Timber.d("Received TimingAppData")
    }.flowOn(Dispatchers.IO)

    fun getTimingData() = webSocketService.observeTimingData().onEach {
        Timber.d("Received TimingData")
    }.flowOn(Dispatchers.IO)

    fun getDriverTelemetry(number: Int) = flow {
        webSocketService.observeTelemetry().flowOn(Dispatchers.IO).collect { data ->
            Timber.d("Received CarData")
            for (entry in data.data!!.entries) {
                emit(entry.cars[number])
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getPreviousData() = flow {
        subscribe()
        val data = webSocketService.observePreviousData().first()
        emit(data)
    }

    fun getPositions() = webSocketService.observeCarPosition().onEach {
        Timber.d("Received PositionData")
    }.flowOn(Dispatchers.IO)
}
