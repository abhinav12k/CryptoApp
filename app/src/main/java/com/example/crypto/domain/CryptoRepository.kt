package com.example.crypto.domain

import com.example.crypto.data.model.Coin
import com.example.crypto.utils.Result

interface CryptoRepository {
    suspend fun fetchCryptoCoins(): Result<List<Coin>>
}