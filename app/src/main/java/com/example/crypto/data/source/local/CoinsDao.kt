package com.example.crypto.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinsDao {
    @Query("SELECT * FROM coins")
   suspend fun getAllCoins(): List<CoinEntity>

    @Upsert
    suspend fun upsertCoins(vararg coin: CoinEntity)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()
}