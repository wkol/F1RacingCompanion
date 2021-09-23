package com.example.f1racingcompanion.api

import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.utils.LiveTimingUtils.createWebSocketUrl

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

interface LiveTimingService {
    @Receive
    fun getEvents(): Flow<WebSocketEvent>
    @Receive
    fun getData(): Flow<String>
    @Send
    fun subscribeToTopics(message: String)

    companion object {
        fun create(token: String): LiveTimingService {

            val logger = HttpLoggingInterceptor().apply {
                level = if(BuildConfig.DEBUG) {
                     HttpLoggingInterceptor.Level.BASIC
                }
                else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            val okHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()

            val protocol = OkHttpWebSocket(
                okHttpClient, OkHttpWebSocket.SimpleRequestFactory(
                    { Request.Builder().url(createWebSocketUrl(token)).build() },
                    { ShutdownReason.GRACEFUL }
                )
            )
            val config = Scarlet.Configuration(
                messageAdapterFactories = listOf(GsonMessageAdapter.Factory()),
                streamAdapterFactories = listOf(CoroutinesStreamAdapterFactory()),
            )
            val scarletInstance = Scarlet(protocol, config)
            return scarletInstance.create<LiveTimingService>()
        }
    }
}