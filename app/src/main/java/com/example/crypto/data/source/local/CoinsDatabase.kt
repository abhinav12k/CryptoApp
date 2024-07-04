package com.example.crypto.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CoinEntity::class], version = 1, exportSchema = false)
abstract class CoinsDatabase : RoomDatabase() {
    abstract fun getCoinsDao(): CoinsDao
}