package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.ErgastService
import com.example.f1racingcompanion.data.nextsessiondto.EventSessionDto
import com.example.f1racingcompanion.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ErgastRepository @Inject constructor(private val api: ErgastService) {
    fun getNextSession(): Flow<Result<EventSessionDto>> = flow {
        try {
            emit(Result.Loading())
            val response = api.getNextSession()
            emit(Result.Success(response))
        } catch (e: IOException) {
            emit(Result.Error(msg = e.localizedMessage ?: "Unknown error"))
        } catch (e: HttpException) {
            emit(Result.Error("Unable to connect to the server"))
        }
    }.flowOn(Dispatchers.IO)
}
