package com.example.f1racingcompanion.api

import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.data.Subscribe
import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.previousdata.PreviousData
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import com.example.f1racingcompanion.utils.LiveTimingUtils.createWebSocketUrl
import com.example.f1racingcompanion.utils.coroutineadapter.CoroutinesStreamAdapterFactory
import com.example.f1racingcompanion.utils.moshiadapter.CustomMoshiMessageAdapter
import com.example.f1racingcompanion.utils.moshiadapter.RequireType
import com.serjltt.moshi.adapters.FirstElement
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow
import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

interface LiveTimingService {
    @Receive
    fun observeEvents(): Flow<WebSocketEvent>

    @Receive
    @RequireType(type = PreviousData::class)
    @Wrapped(path = ["R"])
    fun observePreviousData(): Flow<PreviousData>

    @Receive
    @RequireType(type = LiveTimingData.LiveCarDataDto::class)
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeTelemetry(): Flow<LiveTimingData<CarDataDto>>

    @Receive
    @RequireType(type = LiveTimingData.LiveTimingAppDataDto::class)
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeTimingAppData(): Flow<LiveTimingData<TimingAppDataDto>>

    @Receive
    @RequireType(type = LiveTimingData.LiveTimingStatsDto::class)
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeTimingStats(): Flow<LiveTimingData<TimingStatsDto>>

    @Receive
    @RequireType(type = LiveTimingData.LiveTimingDataDto::class)
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeTimingData(): Flow<LiveTimingData<TimingDataDto>>

    @Receive
    @RequireType(type = LiveTimingData.LivePositionDataDto::class)
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeCarPosition(): Flow<LiveTimingData<PositionDataDto>>

    @Send
    fun subscribeToTopics(message: Subscribe)

    companion object {

        fun create(
            token: String,
            cookie: Cookie,
            moshi: Moshi,
        ): LiveTimingService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BASIC
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    }
                ).build()

            val protocol = OkHttpWebSocket(
                okHttpClient,
                OkHttpWebSocket.SimpleRequestFactory(
                    {
                        Request.Builder().url(createWebSocketUrl(token))
                            .header("Connection", "keep-alive, Upgrade")
                            .header("User-agent", "BestHTTP")
                            .addHeader("Accept-Encoding", "gzip, identity")
                            .addHeader("cookie", "GCLB=${cookie.value}")
                            .addHeader("Host", "livetiming.formula1.com")
                            .build()
                    },
                    { ShutdownReason.GRACEFUL }
                )
            )
            val config = Scarlet.Configuration(
                messageAdapterFactories = listOf(CustomMoshiMessageAdapter.Factory(moshi)),
                streamAdapterFactories = listOf(CoroutinesStreamAdapterFactory()),
            )
            val scarletInstance = Scarlet(protocol, config)
            return scarletInstance.create()
        }
    }
}
