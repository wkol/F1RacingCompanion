package com.example.f1racingcompanion.api

import com.example.f1racingcompanion.data.negotiateDto.NegotiateDto
import com.example.f1racingcompanion.data.streamingstatusdto.SessionStatusDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LiveTimingFormula1Service {
    @GET("negotiate")
    suspend fun negotiate(
        @Query("connectionData", encoded = false) hubName: String,
        @Query("clientProtocol", encoded = false) clientProtocol: String = "1.5"
    ): NegotiateDto

    @GET("static/StreamingStatus.json")
    suspend fun streamingStatus(): SessionStatusDto
}
