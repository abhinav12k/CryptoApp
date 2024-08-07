package com.example.crypto.data.source.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinsLocalDataSource @Inject constructor(private val coinsDao: CoinsDao) {

    suspend fun getCoinsFromDb(): List<CoinEntity> = withContext(Dispatchers.IO) {
        coinsDao.getAllCoins()
    }

    suspend fun clearOldCoinsAndSaveNewToDb(coins: List<CoinEntity>) = withContext(Dispatchers.IO) {
        deleteAllCoins()
        saveCoinsToDb(coins)
    }

    suspend fun saveCoinsToDb(coins: List<CoinEntity>) = withContext(Dispatchers.IO) {
        coinsDao.upsertCoins(*coins.toTypedArray())
    }

    suspend fun deleteAllCoins() = withContext(Dispatchers.IO) {
        coinsDao.deleteAllCoins()
    }
}