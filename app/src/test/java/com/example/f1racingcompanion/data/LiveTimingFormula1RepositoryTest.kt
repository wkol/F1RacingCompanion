package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingFormula1Service
import com.example.f1racingcompanion.utils.DateParser
import com.example.f1racingcompanion.utils.Result
import com.serjltt.moshi.adapters.FirstElement
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class LiveTimingFormula1RepositoryTest {

    private lateinit var liveTimingFormula1Service: LiveTimingFormula1Service
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi
    private lateinit var okHttpClient: OkHttpClient
    private val url by lazy { mockWebServer.url("/") }

    @Before
    fun setUpMockServer() {
        mockWebServer = MockWebServer()
        moshi = Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).add(FirstElement.ADAPTER_FACTORY)
            .add(DateParser()).add(KotlinJsonAdapterFactory())
            .build()
        okHttpClient = OkHttpClient.Builder().build()
        liveTimingFormula1Service = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(LiveTimingFormula1Service::class.java)
    }

    @Test
    fun getConnectionToken_success_returnsValidToken() = runBlocking {
        val liveTimingFormula1Repository = LiveTimingFormula1Repository(liveTimingFormula1Service)

        mockWebServer.enqueue(
            MockResponse().setBody(
                "{\n" +
                    "    \"Url\": \"/signalr\",\n" +
                    "    \"ConnectionToken\": \"546732\",\n" +
                    "    \"ConnectionId\": \"123\",\n" +
                    "    \"KeepAliveTimeout\": 20.0,\n" +
                    "    \"DisconnectTimeout\": 30.0,\n" +
                    "    \"ConnectionTimeout\": 110.0,\n" +
                    "    \"TryWebSockets\": true,\n" +
                    "    \"ProtocolVersion\": \"1.5\",\n" +
                    "    \"TransportConnectTimeout\": 10.0,\n" +
                    "    \"LongPollDelay\": 1.0\n" +
                    "}"
            )
        )
        val response = liveTimingFormula1Repository.getConnectionToken().take(2).toList()
        val request = mockWebServer.takeRequest()

        assertTrue(response.first() is Result.Loading)
        assertEquals("546732", response[1].data!!)
        assertEquals(
            "/signalr/negotiate?connectionData=%5B%7B%22name%22%3A%20%22Streaming%22%7D%5D&clientProtocol=1.5",
            request.path
        )
    }

    @Test
    fun checkForActiveSession_success_shouldReturnTrue() = runBlocking {
        val liveTimingFormula1Repository = LiveTimingFormula1Repository(liveTimingFormula1Service)

        mockWebServer.enqueue(
            MockResponse().setBody("{\"ArchiveStatus\":{\"Status\": \"Generating\"}}")
        )

        val response = liveTimingFormula1Repository.checkForActiveSession().take(2).toList()
        val request = mockWebServer.takeRequest()

        assertEquals(true, response[1].data!!)
        assertEquals(
            "/static/StreamingStatus.json",
            request.path
        )
    }
}
