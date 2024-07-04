package com.example.crypto.di

import com.example.crypto.data.source.network.CryptoApi
import com.example.crypto.utils.GsonHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://37656be98b8f42ae8348e4da3ee3193f.api.mockbin.io/")
            .addConverterFactory(GsonConverterFactory.create(GsonHelper.gson))
            .build()
    }

    @Provides
    fun provideCryptoApi(retrofit: Retrofit): CryptoApi {
        return retrofit.create(CryptoApi::class.java)
    }
}