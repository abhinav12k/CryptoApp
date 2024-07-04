package com.example.crypto.di

import android.content.Context
import com.craftzlabs.analytics.AnalyticsManager
import com.craftzlabs.analytics.AnalyticsService
import com.craftzlabs.analytics.MixpanelAnalyticsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalyticsService(@ApplicationContext context: Context): AnalyticsService {
        return AnalyticsManager.Builder
            .registerAnalyticsService(MixpanelAnalyticsService())
            .build(context)
    }
}