package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingProxyService
import com.example.f1racingcompanion.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LiveTimingProxyRepository @Inject constructor(
    private val api: LiveTimingProxyService
) {

    fun getSyncedData() = flow {
        try {
            emit(Result.Loading())
            val response = api.syncLiveTimingData()
            emit(Result.Success(response))
        } catch (e: IOException) {
            emit(Result.Error(msg = e.localizedMessage ?: "Unknown error"))
        } catch (e: HttpException) {
            emit(Result.Error("Unable to connect to server"))
        }
    }.flowOn(Dispatchers.IO)
}
