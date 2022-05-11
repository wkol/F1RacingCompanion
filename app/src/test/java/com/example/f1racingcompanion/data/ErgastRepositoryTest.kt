package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.ErgastService
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
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ErgastRepositoryTest {

    private lateinit var ergastService: ErgastService
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
        ergastService = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ErgastService::class.java)
    }

    @Test
    fun getNextSession_success_returnsSession() = runBlocking {
        val ergastRepository = ErgastRepository(ergastService)

        mockWebServer.enqueue(MockResponse().setBody("{\"MRData\":{\"xmlns\":\"http:\\/\\/ergast.com\\/mrd\\/1.5\",\"series\":\"f1\",\"url\":\"http://ergast.com/api/f1/current/next.json\",\"limit\":\"30\",\"offset\":\"0\",\"total\":\"1\",\"RaceTable\":{\"season\":\"2022\",\"round\":\"6\",\"Races\":[{\"season\":\"2022\",\"round\":\"6\",\"url\":\"http:\\/\\/en.wikipedia.org\\/wiki\\/2022_Spanish_Grand_Prix\",\"raceName\":\"Spanish Grand Prix\",\"Circuit\":{\"circuitId\":\"catalunya\",\"url\":\"http:\\/\\/en.wikipedia.org\\/wiki\\/Circuit_de_Barcelona-Catalunya\",\"circuitName\":\"Circuit de Barcelona-Catalunya\",\"Location\":{\"lat\":\"41.57\",\"long\":\"2.26111\",\"locality\":\"Montmeló\",\"country\":\"Spain\"}},\"date\":\"2022-05-22\",\"time\":\"13:00:00Z\",\"FirstPractice\":{\"date\":\"2022-05-20\",\"time\":\"12:00:00Z\"},\"SecondPractice\":{\"date\":\"2022-05-20\",\"time\":\"15:00:00Z\"},\"ThirdPractice\":{\"date\":\"2022-05-21\",\"time\":\"11:00:00Z\"},\"Qualifying\":{\"date\":\"2022-05-21\",\"time\":\"14:00:00Z\"}}]}}}"))

        val response = ergastRepository.getNextSession().take(2).toList()
        val request = mockWebServer.takeRequest()

        Assert.assertTrue(response.first() is Result.Loading)
        assertEquals(null, response[1].data!!.sprint)
        assertEquals("2022-05-20", response[1].data!!.secondPractice!!.date)
        assertEquals("Circuit de Barcelona-Catalunya", response[1].data!!.circuitInfo.circuitName)
        assertEquals("/current/next.json", request.path)
    }
}
