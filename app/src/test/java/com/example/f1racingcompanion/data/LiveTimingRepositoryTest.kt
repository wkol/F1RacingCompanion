package com.example.f1racingcompanion.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.f1racingcompanion.api.LiveTimingService
import com.example.f1racingcompanion.utils.DateParser
import com.example.f1racingcompanion.utils.FlowStreamAdapter
import com.example.f1racingcompanion.utils.LiveTimingDataParser
import com.serjltt.moshi.adapters.FirstElement
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket
import io.fabric8.mockwebserver.DefaultMockServer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After

import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Rule


@ExperimentalCoroutinesApi
class LiveTimingRepositoryTest {
    private lateinit var moshi: Moshi

    private val server = DefaultMockServer()

    private val url by lazy { server.url("/test") }

    private lateinit var service: LiveTimingService

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpMockWebSocket() {
        moshi = Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).add(FirstElement.ADAPTER_FACTORY).add(LiveTimingDataParser.Factory).add(DateParser()).add(KotlinJsonAdapterFactory()).build()
        server.start()
        val okHttpClient = OkHttpClient.Builder().build()
        val protocol = OkHttpWebSocket(
            okHttpClient,
            OkHttpWebSocket.SimpleRequestFactory(
                { Request.Builder().url(url).build() },
                { ShutdownReason.GRACEFUL }
            )
        )
        val config = Scarlet.Configuration(
            messageAdapterFactories = listOf(MoshiMessageAdapter.Factory(moshi)),
            streamAdapterFactories = listOf(FlowStreamAdapter.Factory())
        )
        val scarletInstance = Scarlet(protocol, config)
        service = scarletInstance.create()
    }

    @After
    fun stopServer() = server.shutdown()

    @Test
    fun openAndCloseEvents_connectingToServer() = runBlocking {
        // Given a set up repository and a mocked webSocket server
        val repository = LiveTimingRepository(service)
        server.expect().get().withPath("/test")
            .andUpgradeToWebSocket()
            .open()
            .done()
            .once()

        // When starting a webSocketConnection
        val events = repository.startWebSocket().takeWhile { it !is WebSocketEvent.OnConnectionClosed }.toList()

        // Then the repository opens and closes connection
        assert(events[0] is WebSocketEvent.OnConnectionOpened)
        assert(events[1] is WebSocketEvent.OnConnectionClosing)
    }

    @Test
    fun getTimingStats() = runBlocking {
        val repository = LiveTimingRepository(service)
        val message = "{\"C\":\"7,1299\",\"M\":[{\"H\":\"Streaming\",\"M\":\"feed\",\"A\":" +
                "[\"TimingStats\", {\"Lines\": {\"5\": {\"BestSpeeds\": {\"ST\": {\"Value\": \"260\"}}}," +
                "\"16\": {\"BestSpeeds\": {\"ST\": {\"Position\": 2, \"Value\": \"293\"}}}}}, \"2021-10-08T08:34:53.943Z\"]}]}"
        server.expect().get().withPath("/test")
            .andUpgradeToWebSocket()
            .open()
            .waitFor(0).andEmit(message)
            .done()
            .once()

        val element = repository.getTimingStats().first()

        assertEquals(element.date.toString(), "Fri Oct 08 08:34:53 CEST 2021")
        assertEquals(element.data!!.lines[5]!!.bestSpeeds!!["ST"]!!.value, "260")

    }

    @Test
    fun getTimingData() = runBlocking {
        val repository = LiveTimingRepository(service)
        val message = "{\"C\":\"d-16F253A7-B,0|MNW,0|MNX,11|BG,EB|BM,DA0|BL,DA0|F,2|S,3A|GU,0|BK,1DB|R,121|P,3B|Q,5|6,46|BI,15|BD,1|BO,6|N,0|7,1299\",\"M\":[{\"H\":\"Streaming\",\"M\":\"feed\",\"A\":[\"TimingData\",{\"Lines\":{\"16\":{\"Speeds\":{\"ST\":{\"Value\":\"290\"}}}}},\"2021-10-23T08:34:53.834Z\"]}]}"
            server.expect().get().withPath("/test")
            .andUpgradeToWebSocket()
            .open()
                .waitFor(0).andEmit(message)
            .done()
            .once()

        val element = repository.getTimingData().first()

        assertEquals(element.date.toString(), "Sat Oct 23 08:34:53 CEST 2021")
        assertEquals(element.data!!.lines[16]!!.speeds!!["ST"]!!.value, "290")


    }
//
//    @Test
//    fun getDriverTelemetry() {
//    }
//
//    @Test
//    fun getPositions() {
//    }
}