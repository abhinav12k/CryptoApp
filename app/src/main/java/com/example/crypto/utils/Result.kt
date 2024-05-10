package com.example.crypto.utils

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Failure<out T : Any>(val errorBody: ErrorBody) : Result<T>()
    data object SuccessWithNoResult : Result<Nothing>()
}

data class ErrorBody(val message: String, val code: Int = -1, val rawMessage: String = "")

const val UNABLE_TO_CONNECT_TO_INTERNET = "Unable to connect with the server. Please check your internet connection"