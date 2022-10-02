package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.liveTimingData.PreviousData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.DateParser
import com.example.f1racingcompanion.utils.LiveTimingUtils
import com.example.f1racingcompanion.utils.PreviousDataParser
import com.example.f1racingcompanion.utils.parsers.LiveTimingDataParser
import com.example.f1racingcompanion.utils.parsers.LiveTimingListParser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.websocket.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class LiveTimingRepository(private val webSocketService: LiveTimingService) {

    private val moshi: Moshi =
        Moshi.Builder().add(LiveTimingListParser.Factory).add(PreviousDataParser.Factory)
            .add(LiveTimingDataParser.Factory)
            .add(KotlinJsonAdapterFactory()).add(DateParser()).build()

    private var _isOpen: Boolean = false
    val isOpen: Boolean
        get() = _isOpen

    private val allMessages = webSocketService.observeLiveTimingData().transform { element ->
        element.forEach { emit(it) }
    }.flowOn(Dispatchers.IO)
        .shareIn(CoroutineScope(Dispatchers.IO), SharingStarted.WhileSubscribed(5000))

    private fun subscribe() {
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

    fun getTimingStats() =
        allMessages.transform { if (it is LiveTimingData.LiveTimingStatsDto) emit(it) }.onEach {
            Timber.d("Received TimingStats")
        }.flowOn(Dispatchers.IO)

    fun getTimingAppData() =
        allMessages.transform { if (it is LiveTimingData.LiveTimingAppDataDto) emit(it) }.onEach {
            Timber.d("Received TimingAppData")
        }.flowOn(Dispatchers.IO)

    fun getTimingData() =
        allMessages.transform { if (it is LiveTimingData.LiveTimingDataDto) emit(it) }.onEach {
            Timber.d("Received TimingData")
        }.flowOn(Dispatchers.IO)

    fun getDriverTelemetry() = allMessages.filter { it?.type == Constants.LiveTimingDataType.CAR_DATA }.map {
        LiveTimingData.LiveTimingBulkMessage(
            LiveTimingUtils.decodeMessage((it as LiveTimingData.LiveTimingBulkMessage).data),
            Constants.LiveTimingDataType.CAR_DATA,
            it.date
        )
    }.flowOn(Dispatchers.Default).transform {
        Timber.d("Received DriverTelemetry")
        emit(
            LiveTimingData.LiveCarDataDto(
                moshi.adapter(CarDataDto::class.java).fromJson(it.data),
                it.date
            )
        )
    }.flowOn(Dispatchers.IO)

    fun getPreviousData() = allMessages.transform { if (it is PreviousData) emit(it) }.onEach {
        Timber.d("Received PreviousData")
    }.flowOn(Dispatchers.IO)

    fun getPositions() = allMessages.filter { it?.type == Constants.LiveTimingDataType.POSITION_DATA }.map {
        LiveTimingData.LiveTimingBulkMessage(
            LiveTimingUtils.decodeMessage((it as LiveTimingData.LiveTimingBulkMessage).data),
            Constants.LiveTimingDataType.POSITION_DATA,
            it.date
        )
    }.flowOn(Dispatchers.Default).transform {
        Timber.d("Received PositionData")
        emit(
            LiveTimingData.LivePositionDataDto(
                moshi.adapter(PositionDataDto::class.java)
                    .fromJson(it.data),
                it.date
            )
        )
    }.flowOn(Dispatchers.IO)
}
