package com.example.f1racingcompanion.api

import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.data.StreamingStatusDto
import com.example.f1racingcompanion.data.negotiateDto.NegotiateDto
import com.example.f1racingcompanion.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Formula1Service {
    @GET("negotiate")
    suspend fun negotiate(
        @Query("connectionData", encoded = false) hubName: String,
        @Query("clientProtocol", encoded = false) clientProtocol: String = "1.5"
        ): NegotiateDto

    @GET("static/StreamingStatus.json")
    suspend fun streamingStatus(): StreamingStatusDto

    companion object {

        fun create(): Formula1Service {
            val logger = HttpLoggingInterceptor().apply {
                level = if(BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                }
                else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.LIVETIMING_NEGOTIATE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Formula1Service::class.java)
        }
    }
}