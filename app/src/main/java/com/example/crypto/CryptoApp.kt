package com.example.crypto

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoApp : Application() {

    lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit
            .Builder()
            .baseUrl(UPSTREAM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        private const val UPSTREAM_URL = "https://37656be98b8f42ae8348e4da3ee3193f.api.mockbin.io/"
    }
}