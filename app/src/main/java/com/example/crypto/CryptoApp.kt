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
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltAndroidApp
class CryptoApp : Application() {

    @Inject
    lateinit var analyticsService: AnalyticsService

    override fun onCreate() {
        super.onCreate()
        analyticsService.trackEvent("app_opened")
    }
}