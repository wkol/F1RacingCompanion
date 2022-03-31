package com.example.f1racingcompanion.api

import com.example.f1racingcompanion.data.f1driverlistelementdto.F1DriverListElementDto
import retrofit2.http.GET

interface LiveTimingProxyService {
    @GET("api/liveData/")
    suspend fun syncLiveTimingData(): List<F1DriverListElementDto>
}
