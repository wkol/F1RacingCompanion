package com.example.f1racingcompanion.api

import android.app.Application
import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.data.Subscribe
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.utils.LiveTimingUtils.createWebSocketUrl
import com.example.f1racingcompanion.utils.coroutineadapter.CoroutinesStreamAdapterFactory
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
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
    fun observeLiveTimingData(): Flow<List<LiveTimingData?>>

    @Send
    fun subscribeToTopics(message: Subscribe)

    companion object {

        fun create(
            token: String,
            cookie: Cookie,
            moshi: Moshi,
            app: Application,
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
                messageAdapterFactories = listOf(MoshiMessageAdapter.Factory(moshi)),
                streamAdapterFactories = listOf(CoroutinesStreamAdapterFactory()),
                lifecycle = AndroidLifecycle.ofApplicationForeground(app)
            )
            val scarletInstance = Scarlet(protocol, config)
            return scarletInstance.create()
        }
    }
}
