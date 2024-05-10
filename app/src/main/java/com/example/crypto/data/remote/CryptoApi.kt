package com.example.crypto.data.remote

import com.example.crypto.data.model.Coin
import retrofit2.Response
import retrofit2.http.GET

interface CryptoApi {
    @GET("/")
    suspend fun getCoinData(): Response<List<Coin>>
}