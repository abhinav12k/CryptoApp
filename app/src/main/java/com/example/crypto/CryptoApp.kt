package com.example.crypto

import android.app.Application
import androidx.room.Room
import com.craftzlabs.analytics.AnalyticsManager
import com.craftzlabs.analytics.AnalyticsService
import com.craftzlabs.analytics.MixpanelAnalyticsService
import com.example.crypto.data.CryptoRepository
import com.example.crypto.data.source.local.CoinsDatabase
import com.example.crypto.data.source.local.CoinsLocalDataSource
import com.example.crypto.data.source.network.CoinsNetworkSource
import com.example.crypto.data.source.network.CryptoApi
import com.example.crypto.utils.GsonHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoApp : Application() {

    private lateinit var retrofit: Retrofit
    private lateinit var cryptoApi: CryptoApi
    lateinit var repository: CryptoRepository
    lateinit var analyticsService: AnalyticsService

    override fun onCreate() {
        super.onCreate()

        analyticsService = AnalyticsManager.Builder
            .registerAnalyticsService(MixpanelAnalyticsService())
            .build(this)
        analyticsService.trackEvent("app_opened")

        retrofit = Retrofit
            .Builder()
            .baseUrl(UPSTREAM_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonHelper.gson))
            .build()

        database = Room.databaseBuilder(
            applicationContext,
            CoinsDatabase::class.java,
            "coins_db"
        ).build()

        cryptoApi = retrofit.create(CryptoApi::class.java)
        repository = CryptoRepository(
            CoinsNetworkSource(cryptoApi),
            CoinsLocalDataSource(database.getCoinsDao())
        )
    }

    companion object {
        private const val UPSTREAM_URL = "https://37656be98b8f42ae8348e4da3ee3193f.api.mockbin.io/"
        lateinit var database: CoinsDatabase
    }
}