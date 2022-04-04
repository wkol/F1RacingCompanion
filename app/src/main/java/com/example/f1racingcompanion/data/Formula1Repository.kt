package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.Formula1Service
import com.example.f1racingcompanion.data.nextsessiondto.EventTrackerDto
import com.example.f1racingcompanion.data.nextsessiondto.NextSessionDto
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class Formula1Repository @Inject constructor(private val api: Formula1Service) {
    fun getNextSession(): Flow<Result<EventTrackerDto>> = flow {
        try {
            emit(Result.Loading())
            val response = api.getNextSession(Constants.API_KEY, Constants.API_LOCALE)
            emit(Result.Success(response))
        } catch (e: IOException) {
            emit(Result.Error(msg = e.localizedMessage ?: "Unknown error"))
        } catch (e: HttpException) {
            emit(Result.Error("Unable to connect to the server"))
        }
    }.flowOn(Dispatchers.IO)
}