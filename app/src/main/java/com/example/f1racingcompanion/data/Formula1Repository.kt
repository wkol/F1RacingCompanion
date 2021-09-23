package com.example.f1racingcompanion.data

import com.example.f1racingcompanion.api.Formula1Service
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
class Formula1Repository @Inject constructor(
    private val api: Formula1Service
) {

    fun getConnectionToken(hubData: String): Flow<Result<String>> = flow {
        try {
            emit(Result.Loading<String>())
            val response = api.negotiate(hubData)
            emit(Result.Success<String>(response.connectionToken))
        } catch (e: IOException) {
            emit(Result.Error<String>(msg = e.localizedMessage ?: "Unknown error"))
        } catch (e: HttpException) {
            emit(Result.Error<String>("Unable to connect to the server"))
        }
    }.flowOn(Dispatchers.IO)

    fun checkForActiveSession(): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading<Boolean>())
            val response = api.streamingStatus()
            emit(
                Result.Success<Boolean>(
                    data = (when (response.status) {
                        "Offline" -> false
                        "Online" -> true
                        else -> null
                    })))
        } catch (e: IOException) {
            emit(Result.Error<Boolean>(msg = e.localizedMessage ?: "Unknown error"))
        } catch (e: HttpException) {
            emit(Result.Error<Boolean>("Unable to connect to the server"))
        }
    }.flowOn(Dispatchers.IO)

}