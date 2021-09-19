package com.example.f1racingcompanion.di.data

import com.example.f1racingcompanion.di.data.negotiateDto.NegotiateDto
import retrofit2.http.GET
import retrofit2.http.Query

interface Formula1LiveTimingApi {
    @GET("negotiate")
    suspend fun negeotiate(
        @Query("connectionData", encoded = false) hubName: String,
        @Query("clientProtocol", encoded = false) clientProtocol: String = "1.5"
        ): NegotiateDto

    @GET("static/StreamingStatus.json")
    suspend fun streamingStatus(): StreamingStatusDto
}