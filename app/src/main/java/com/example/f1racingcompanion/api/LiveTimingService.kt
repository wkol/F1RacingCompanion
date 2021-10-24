package com.example.f1racingcompanion.api

import android.app.Application
import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.data.Subscribe
import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import com.example.f1racingcompanion.utils.LiveTimingUtils.createWebSocketUrl
import com.serjltt.moshi.adapters.FirstElement
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

interface LiveTimingService {
    @Receive
    fun observeEvents(): Flow<WebSocketEvent>

    @Receive
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeTelemetry(): Flow<LiveTimingData<CarDataDto>>

    @Receive
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeTimingStats(): Flow<LiveTimingData<TimingStatsDto>>

    @Receive
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeTimingData(): Flow<LiveTimingData<TimingDataDto>>

    @Receive
    @Wrapped(path = ["M"])
    @FirstElement
    fun observeCarPosition(): Flow<LiveTimingData<PositionDataDto>>

    @Send
    fun subscribeToTopics(message: Subscribe)

    companion object {

        @ExperimentalCoroutinesApi
        fun create(token: String, cookie: Cookie, app: Application, moshi: Moshi): LiveTimingService {
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
                messageAdapterFactories = listOf(MoshiMessageAdapter.Factory(moshi)),
                streamAdapterFactories = listOf(com.example.f1racingcompanion.utils.FlowStreamAdapter.Factory()),
                lifecycle = AndroidLifecycle.ofApplicationForeground(app)
            )
            val scarletInstance = Scarlet(protocol, config)
            return scarletInstance.create<LiveTimingService>()
        }
    }
}
