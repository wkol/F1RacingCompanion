package com.example.f1racingcompanion.api

import com.example.f1racingcompanion.data.nextsessiondto.EventTrackerDto
import com.example.f1racingcompanion.data.nextsessiondto.NextSessionDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Formula1Service {
    @GET("event-tracker")
    suspend fun getNextSession(@Header("apikey") apiKey: String, @Header("locale") locale: String): EventTrackerDto
}