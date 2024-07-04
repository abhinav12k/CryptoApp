package com.example.crypto.data.source.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoinsNetworkSource @Inject constructor(private val api: CryptoApi) {

    suspend fun getLatestCoins() = withContext(Dispatchers.IO) {
        api.getCoinData()
    }

}