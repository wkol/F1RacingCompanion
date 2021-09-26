package com.example.f1racingcompanion.api

import android.app.Application
import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.data.Subscribe
import com.example.f1racingcompanion.utils.LiveTimingUtils.createWebSocketUrl
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

interface LiveTimingService {
    @Receive
    fun observeEvents(): Flow<WebSocketEvent>

    @Receive
    fun observeData(): Flow<Array<String>>
    @Send
    fun subscribeToTopics(message: Subscribe)

    companion object {

        fun create(token: String, cookie: Cookie, app: Application): LiveTimingService {


            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BASIC
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }).build()

            val protocol = OkHttpWebSocket(
                okHttpClient, OkHttpWebSocket.SimpleRequestFactory(
                    { Request.Builder().url(createWebSocketUrl(token))
                        .header("Connection", "keep-alive, Upgrade")
                        .header("User-agent","BestHTTP")
                        .addHeader("Accept-Encoding", "gzip, identity")
                        .addHeader("cookie", "GCLB=${cookie.value}")
                        .addHeader("Host", "livetiming.formula1.com")
                        .build()},
                    { ShutdownReason.GRACEFUL }
                )
            )
            val config = Scarlet.Configuration(
                messageAdapterFactories = listOf(GsonMessageAdapter.Factory()),
                streamAdapterFactories = listOf(com.example.f1racingcompanion.utils.FlowStreamAdapter.Factory()),
                lifecycle = AndroidLifecycle.ofApplicationForeground(app)
            )
            val scarletInstance = Scarlet(protocol, config)
            return scarletInstance.create<LiveTimingService>()
        }
    }
}