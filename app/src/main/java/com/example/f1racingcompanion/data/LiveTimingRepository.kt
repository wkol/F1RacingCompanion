package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.LiveTimingUtils.decodeMessage
import com.squareup.moshi.Moshi
import com.tinder.scarlet.websocket.WebSocketEvent
import kotlinx.coroutines.flow.*
import timber.log.Timber

class LiveTimingRepository (private val webSocketService: LiveTimingService, private val moshi: Moshi) {

    fun startWebSocket() = flow {
        webSocketService.observeEvents().collect { event ->
            if(event is WebSocketEvent.OnConnectionOpened) {
                webSocketService.subscribeToTopics(
                    Subscribe(
                    "Streaming",
                    "Subscribe",
                    listOf(Constants.SUBSRIBED_STREAMS),
                    0
                ))
                Timber.d("Send subscribe request")
            }
            emit(event)
            Timber.d("Received event: ${event::class.java.simpleName}")
        }
    }

    fun getData() = flow {
        webSocketService.observeData().collect { data ->
            Timber.d("Received data in from of $data")
            for(line in data) {
                emit(when(line.messageData[0]) {
                    "CarData.z" -> moshi.adapter(CarDataDto::class.java).fromJson(decodeMessage(line.messageData[1]))!!
                    "Position.z" -> moshi.adapter(PositionDataDto::class.java).fromJson(decodeMessage(line.messageData[1]))!!
                    // Ignore invalid data
                    else -> continue
                })
            }
        }
    }

}