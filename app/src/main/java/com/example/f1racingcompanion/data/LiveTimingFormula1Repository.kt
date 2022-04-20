package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.LiveTimingFormula1Service
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveTimingFormula1Repository @Inject constructor(
    private val api: LiveTimingFormula1Service
) {

    fun getConnectionToken(hubData: String = Constants.HUB_DATA): Flow<Result<String>> = flow {
        try {
            emit(Result.Loading())
            val response = api.negotiate(hubData)
            emit(Result.Success(response.connectionToken))
        } catch (e: IOException) {
            emit(Result.Error(msg = e.localizedMessage ?: "Unknown error"))
        } catch (e: HttpException) {
            emit(Result.Error("Unable to connect to the server"))
        }
    }.flowOn(Dispatchers.IO)

    fun checkForActiveSession(): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading())
            val response = api.streamingStatus()
            emit(
                Result.Success(
                    data = when (response.status) {
                        "Offline" -> false
                        "Online" -> true
                        else -> null
                    }
                )
            )
        } catch (e: IOException) {
            emit(Result.Error(msg = e.localizedMessage ?: "Unknown error"))
        } catch (e: HttpException) {
            emit(Result.Error("Unable to connect to the server"))
        }
    }.flowOn(Dispatchers.IO)
}
