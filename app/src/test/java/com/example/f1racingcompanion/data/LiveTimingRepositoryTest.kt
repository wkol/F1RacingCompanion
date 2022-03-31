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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LiveTimingRepositoryTest {
    private lateinit var moshi: Moshi

    private lateinit var server: DefaultMockServer

    private val url by lazy { server.url("/test") }

    private lateinit var service: LiveTimingService

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpMockWebSocket() {
        server = DefaultMockServer()
        moshi = Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).add(FirstElement.ADAPTER_FACTORY)
            .add(LiveTimingDataParser.Factory).add(DateParser()).add(KotlinJsonAdapterFactory())
            .build()
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
        val events =
            repository.startWebSocket().take(3).toList()

        // Then the repository opens and closes connection
        assertTrue(events[0] is WebSocketEvent.OnConnectionOpened)
        assertTrue(events[1] is WebSocketEvent.OnConnectionClosing)
        assertTrue(events[2] is WebSocketEvent.OnConnectionClosed)
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

        assertEquals(element.date!!.dayOfMonth, 8)
        assertEquals(element.date!!.second, 53)
        assertEquals(element.date!!.minute, 34)
        assertEquals(element.data!!.lines[5]!!.bestSpeeds!!["ST"]!!.value, "260")
    }

    @Test
    fun getTimingData() = runBlocking {
        val repository = LiveTimingRepository(service)
        val message =
            "{\"C\":\"d-16F253A7-B,0|MNW,0|MNX,11|BG,EB|BM,DA0|BL,DA0|F,2|S,3A|GU,0|BK,1DB|R,121|P,3B|Q,5|6,46|BI,15|BD,1|BO,6|N,0|7,1299\",\"M\":[{\"H\":\"Streaming\",\"M\":\"feed\",\"A\":[\"TimingData\",{\"Lines\":{\"16\":{\"Speeds\":{\"ST\":{\"Value\":\"290\"}}}}},\"2021-10-23T08:34:53.834Z\"]}]}"
        server.expect().get().withPath("/test")
            .andUpgradeToWebSocket()
            .open()
            .waitFor(0).andEmit(message)
            .done()
            .once()

        val element = repository.getTimingData().first()
        assertEquals(element.date!!.dayOfMonth, 23)
        assertEquals(element.date!!.second, 53)
        assertEquals(element.date!!.minute, 34)
        assertEquals(element.data!!.lines[16]!!.speeds!!["ST"]!!.value, "290")
    }

    @Test
    fun getDriverTelemetry() = runBlocking {
        val repository = LiveTimingRepository(service)
        val message =
            "{\"C\":\"d-BAF194C6-B,0|QI,0|QJ,11|BM,171|BT,155C|BS,155B|F,5|k,32D|Ml,0|BR,349|j,17B|h,5C|i,3|BO,57|BP,17|BJ,1|BV,16|f,12|BW,43F6\",\"M\":[{\"H\":\"Streaming\",\"M\":\"feed\",\"A\":[\"CarData.z\",\"xZjBbtw4DIbfxedJQVKiSM612DfYXlrsoVgE2AUWOXR7C/LutSWqcGJ67FEOuWQQw79EU+RHUs/TH08/f/z7+P90/fY8ffn593SdCAgfEB4o/4l2TelK9slEiqF9nS7T5+8/5refp7T8+fzP96enx//qA5iuCJLSZaLpSiVfllfkMuXpanqZeLrC/M/y8/JSn27lCLnJMWGVU31R0kbOgdwwaVUrVDFWMW/FJRAX4LQWpyom3oglEIui2124qktVQxUjuFwXucVu02Y5mVW9Vn1VvtkeIVgAqrgZDi7NvnluYqxiDL2eF28vu2dZWR/uHh+bmp+6HxvvLxD5HpG4tAWgrBZYnPFWr7Ee/AuWCLj5BUSxC9APAOTgC1LkQ4HcDNC8ittAHGYNZlcT2sHuOTqAwmQt/KB9fo7Cr+mj8FVa4rymnR3oOUo8RLYeQUf+L5EHjKW5HzWtHFBkm32R/fP5mwTx82Z/pJqAUQZqBmoGYFml/9YD8wq7nEzwiVAKs5zgJHdOygAnwTxgMK9ppXiKk4qebWIrTuZtsoWcTNBiTWnNya045GSR7HbbGCeLOSf1AzhZsFlPTGOctL5AymOc5OQLIAxxMhfnbE5jnFTPk0qM+zlZ1EmlOMJJQfDdj/wXcpIVmvvsCHMhJo08aZHKGCZF4GQAxZhU9LTXNeYkn8WklF5nxjApZl4owN6DycxS5ufHmNSeMEOYnMuZ10XOa0yWc5jMOWont84OMdkbCuXVzvlkO8ns1egVJ+7ApKSPxKT67sxjmCzFm7EUNWOvM20HlMk6KNMWlG9WiFFZUE42xTEqqQ8UlGAIlbk0A0RGUNlRcbx7jMrsmW5prKP0CET3/x4o9lAJWXutPapUISrnF8ktsCMH7MDS+kxGNAZL9mqDQO+BZTFhzeUELLPX5qIjPSV7b4U+uue93iKcvUkogOWWd1G6CnhdtLWjgsE9hKWk4lWZN6F6pqOUj0Ml2G9UHs1NO6iEbv6ryfE8KOdT75P34QI7nDw9+sWcBLVysimOOVlHdmpt0N2chMI+UaQjzsScBC80KiOcFMngjOBBTnbvcVkl/R2c5OyUsqOOfoeTkN2DdOPy4hYnCRs6bOuC05C0JHPJOsFIL2tjjJwHMG/MxO5npJpFjNxecoSMZMecycpLJTilSKzezMxdyX6Y3qKkf/eH3E/OOdoPDQ9ifI+S2GNcxyhZ73U3SZLOU1JLB/0aM0Hg7Mzd/QvIL3z2MRVDErUlqdAKkkvenaOkQe9ljyAfU5JJ19ejQYm+wUgkD14Y7CXZx0Biex8jCQYZidy78RvDxA1GFqW87ZC2jPzr5Rc=\",\"2021-10-24T19:33:30.937Z\"]}]}"
        server.expect().get().withPath("/test")
            .andUpgradeToWebSocket()
            .open()
            .waitFor(0).andEmit(message)
            .done()
            .once()

        val element = repository.getDriverTelemetry(33).first()

        assertEquals(element!!.telemetry.speed, 219)
        assertEquals(element.telemetry.DRSValue, 0)
    }

    @Test
    fun getPositions() = runBlocking {
        val repository = LiveTimingRepository(service)
        val message =
            "{\"C\":\"d-BAF194C6-B,0|QI,0|QJ,11|BM,171|BT,155C|BS,155C|F,5|k,32D|Ml,0|BR,349|j,17B|h,5C|i,3|BO,57|BP,17|BJ,1|BV,16|f,12|BW,43F6\",\"M\":[{\"H\":\"Streaming\",\"M\":\"feed\",\"A\":[\"Position.z\",\"tZc/bxw5DMW/y9S7B/GfSG2fOgeci0sOVxhBCiOIE8R7lbHf/TQjcrIuVtIWbgws4AdKT48/cl6XP3+8PJ2ffjwvp39el4en719fzo/ffy6nBRPCEdIR+QHKiehE6Q8EZMb8eTksH57Pv56+viyn14XWP3+dH8//1Z/Lx+eHX49fvtV/+Xs5cUn5sHxaTkfOcFg+LyfgJJfDwrc1lA2bhpCxiQSsiuS26JiRm0glNw3aqsm3NWjUCmEBaRpiqhrt1EHidiNIgFEIq6jcFmUS2jRMWb1QhqqB1LGOU7sQiO6F8iqC2yIgsrLJaNNvhsum6jh+hJz9UoThXt5UHftMwe1jkXhbXUXWe6fi72TmgaC02ofYOx+oNZVAulZRxwss2EQE8VLEaRV1AgukSTaVFIbrUHDHQBBxAzNQ5BzXWtzJEhGiH1Dk+oDSCXo9n3cUIIYXWtakd6515ATtgU2LP3BZ+1B7WQdm8k5Mcm176YS9AqO0a1mEHU0vl8thzBeuHZ/M5vkiiTwYlHGaL/utIKAkKQ/4IibOF5vni7dw7VuY5gt7VxWxqMMDvICC44XLO+OlGt4iKxI55zFeLLVigHutbCO8GLl7vNacxIuoP1OJSjN4STnwstJpEi/mMae0k2wCL3FAsZJDBWO8aHG8RPom8II+dQj0TakuXgzBcwEQXhiM8CKpOF6YAy8wxAuReiOazuNFfNJbnK++xBxesiAplzvwAhEMzDSNlxTrS4oM8sbaLl7YrTCYx4v4/LXf842HeMktfyW96fqO41J8pWAifGe8ZGjpo5xjpSAZ4iUGPSAEMzWN8FK8O5Byvn7aLl62TedTQwZP86WIj+xs89uLZXa88Jvu7eOFGRwvkq6hNMCLNVVGKoEXGOEFTB0vJtN4KbwbCNHzRiO8ZHC8aJEI03h7QY/FkRXuwAs333Wfv5VkU3gxrR8HQHfgBWMZBgnfEw/wIlIcSSVWOd4c7OIluRXGMo0XbYfD3YkJvESh33Ngq9PDiyV1vOwDJ6f3wYtCa0TSiGz9mh3hBZMnAiDH7qw0wEtJGnjZv8MSjfBCRDF8RafxYuwDR7PO48WXYEpJ5/FC8XFkie/AiybHi92BFx/zFS97KRzgpS6IPka3kdDooiO6qC+aoPHFhzZeXsCzdBS5Z3nxqaOx09av1EqXfy//Aw==\",\"2021-10-24T19:33:30.872Z\"]}]}"
        server.expect().get().withPath("/test")
            .andUpgradeToWebSocket()
            .open()
            .waitFor(0).andEmit(message)
            .done()
            .once()

        val element = repository.getPositions().first()

        assertEquals(element.data!!.entries[0].cars[33]!!.xPosition, 13705.0F)
    }
}
