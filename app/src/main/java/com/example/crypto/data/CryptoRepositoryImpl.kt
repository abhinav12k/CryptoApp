package com.example.crypto.data

import com.example.crypto.data.remote.CryptoApi
import com.example.crypto.domain.CryptoRepository
import com.example.crypto.utils.ErrorBody
import com.example.crypto.utils.GsonHelper.parseToClass
import com.example.crypto.utils.Result
import com.example.crypto.utils.UNABLE_TO_CONNECT_TO_INTERNET
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class CryptoRepositoryImpl(
    private val cryptoApi: CryptoApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CryptoRepository {

    override suspend fun fetchCryptoCoins() = withContext(dispatcher) {
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
        }
    }

    private fun <T> prepareErrorBody(response: Response<T>): ErrorBody {
        val httpCode = response.code()
        val errorBody = response.errorBody()
        val rawMsg = errorBody.toString()
        return rawMsg.parseToClass(ErrorBody::class.java).copy(code = httpCode, rawMessage = rawMsg)
    }
}