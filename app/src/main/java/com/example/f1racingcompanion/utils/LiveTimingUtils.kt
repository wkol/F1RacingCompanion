package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.di.data.Formula1LiveTimingApi

object LiveTimingUtils {
    suspend fun getConnectionToken(api: Formula1LiveTimingApi, hubData: String): String {
        val response = api.negeotiate(hubData)
        return response.connectionToken
    }
    suspend fun checkForActiveSession(api: Formula1LiveTimingApi): Boolean? {
        val response = api.streamingStatus()
        return when(response.status) {
            "Offline" -> false
            "Online" -> true
            else -> null
        }
    }
}