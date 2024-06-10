package com.example.crypto.data.source.network

import retrofit2.Response
import retrofit2.http.GET

interface CryptoApi {
    @GET("/")
    suspend fun getCoinData(): Response<List<CoinDto>>
}