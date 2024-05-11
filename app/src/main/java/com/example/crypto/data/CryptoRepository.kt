package com.example.crypto.data

import com.example.crypto.data.remote.CryptoApi
import com.example.crypto.utils.ErrorBody
import com.example.crypto.utils.GsonHelper.parseToClass
import com.example.crypto.utils.Result
import com.example.crypto.utils.UNABLE_TO_CONNECT_TO_INTERNET
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class CryptoRepository(
    private val cryptoApi: CryptoApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun fetchCryptoCoins() = withContext(dispatcher) {
        try {
            val res = cryptoApi.getCoinData()
            if (res.isSuccessful) {
                val body = res.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.SuccessWithNoResult
                }
            } else {
                Result.Failure(prepareErrorBody(res))
            }
        } catch (ioException: IOException) {
            Result.Failure(ErrorBody(UNABLE_TO_CONNECT_TO_INTERNET))
        } catch (jsonSyntaxException: JsonSyntaxException) {
            Result.Failure(ErrorBody("Error while parsing json"))
        }
    }

    private fun <T> prepareErrorBody(response: Response<T>): ErrorBody {
        val httpCode = response.code()
        val errorBody = response.errorBody()
        val rawMsg = errorBody?.string()
        return rawMsg?.parseToClass(ErrorBody::class.java)!!
            .copy(code = httpCode, rawMessage = rawMsg)
    }
}