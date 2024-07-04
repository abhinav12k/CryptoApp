package com.example.crypto.di

import android.content.Context
import androidx.room.Room
import com.example.crypto.data.source.local.CoinsDao
import com.example.crypto.data.source.local.CoinsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideCoinsDao(database: CoinsDatabase): CoinsDao = database.getCoinsDao()

    @Provides
    @Singleton
    fun provideCoinsDatabase(@ApplicationContext context: Context): CoinsDatabase {
        return Room.databaseBuilder(
            context,
            CoinsDatabase::class.java,
            "coins_db"
        ).build()
    }
}