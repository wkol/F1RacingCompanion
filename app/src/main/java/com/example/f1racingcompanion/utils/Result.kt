package com.example.f1racingcompanion.utils

sealed class Result<T>(val data: T? = null, val msg: String = "") {
    class Success<T>(data: T?) : Result<T>(data)
    class Error<T>(msg: String, data: T? = null) : Result<T>(data, msg)
    class Loading<T>(data: T? = null) : Result<T>(data)
}
