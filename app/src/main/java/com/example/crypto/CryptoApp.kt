package com.example.crypto

import android.app.Application
import com.example.crypto.data.CryptoRepository
import com.example.crypto.data.remote.CryptoApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoApp : Application() {

    private lateinit var retrofit: Retrofit
    private lateinit var cryptoApi: CryptoApi
    lateinit var repository: CryptoRepository

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit
            .Builder()
            .baseUrl(UPSTREAM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        cryptoApi = retrofit.create(CryptoApi::class.java)
        repository = CryptoRepository(cryptoApi)
    }

    companion object {
        private const val UPSTREAM_URL = "https://37656be98b8f42ae8348e4da3ee3193f.api.mockbin.io/"
    }
}