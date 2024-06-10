package com.example.crypto.data

import com.example.crypto.data.model.Coin
import com.example.crypto.data.source.local.CoinEntity
import com.example.crypto.data.source.network.CoinDto
import java.util.UUID

fun CoinDto.toCoin() = Coin(name, symbol, isNew, isActive, type)

fun CoinEntity.toCoin() = Coin(name, symbol, isNew, isActive, type)

fun CoinDto.toCoinEntity() =
    CoinEntity(id = UUID.randomUUID().toString(), name, symbol, isNew, isActive, type)

fun List<CoinDto>.toCoinsViewState() = map { it.toCoin() }

fun List<CoinDto>.toCoinsEntity() = map { it.toCoinEntity() }

fun List<CoinEntity>.toCoins() = map { it.toCoin() }