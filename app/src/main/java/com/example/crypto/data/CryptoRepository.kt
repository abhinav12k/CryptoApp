package com.example.crypto.data

import com.example.crypto.data.source.local.CoinsLocalDataSource
import com.example.crypto.data.source.network.CoinsNetworkSource
import com.example.crypto.utils.ErrorBody
import com.example.crypto.utils.GsonHelper.parseToClass
import com.example.crypto.utils.Result
import com.example.crypto.utils.UNABLE_TO_CONNECT_TO_INTERNET
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

class CryptoRepository(
    private val coinsNetworkSource: CoinsNetworkSource,
    private val coinsLocalDataSource: CoinsLocalDataSource
) {

    fun fetchCoins() = flow {
        var isCacheHit = false
        val coins = coinsLocalDataSource.getCoinsFromDb().map { it.toCoin() }
        if (coins.isNotEmpty()) {
            isCacheHit = true
            emit(Result.Success.Cache(coins))
        }

        when (val networkResult = fetchCryptoCoins()) {
            is Result.Success -> emit(networkResult)
            else -> if (!isCacheHit) emit(networkResult)
        }
    }

    private suspend fun fetchCryptoCoins() = try {
        val res = coinsNetworkSource.getLatestCoins()
        if (res.isSuccessful) {
            val body = res.body()
            if (body != null) {
                coinsLocalDataSource.clearOldCoinsAndSaveNewToDb(body.toCoinsEntity())
                Result.Success.Network(body.toCoinsViewState())
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


    private fun <T> prepareErrorBody(response: Response<T>): ErrorBody {
        val httpCode = response.code()
        val errorBody = response.errorBody()
        val rawMsg = errorBody?.string()
        return rawMsg?.parseToClass(ErrorBody::class.java)!!
            .copy(code = httpCode, rawMessage = rawMsg)
    }
}