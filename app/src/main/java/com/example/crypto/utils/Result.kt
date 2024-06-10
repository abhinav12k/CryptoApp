package com.example.crypto.utils

sealed class Result<out T : Any> {
    sealed class Success<out T : Any>(open val data: T) : Result<T>() {
        data class Network<out T : Any>(override val data: T) : Success<T>(data)
        data class Cache<out T : Any>(override val data: T) : Success<T>(data)
    }

    data class Failure<out T : Any>(val errorBody: ErrorBody) : Result<T>()
    data object SuccessWithNoResult : Result<Nothing>()
}

data class ErrorBody(val message: String?, val code: Int? = -1, val rawMessage: String? = "")

const val UNABLE_TO_CONNECT_TO_INTERNET =
    "Unable to connect with the server. Please check your internet connection"