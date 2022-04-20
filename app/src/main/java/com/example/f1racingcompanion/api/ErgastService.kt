package com.example.f1racingcompanion.api

import com.example.f1racingcompanion.data.nextsessiondto.EventSessionDto
import com.serjltt.moshi.adapters.FirstElement
import com.serjltt.moshi.adapters.Wrapped
import retrofit2.http.GET

interface ErgastService {
    @GET("current/next.json")
    @Wrapped(path = ["MRData", "RaceTable", "Races"])
    @FirstElement
    suspend fun getNextSession(): EventSessionDto
}
