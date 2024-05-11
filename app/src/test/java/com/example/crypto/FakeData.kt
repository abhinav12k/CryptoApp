package com.example.crypto

import com.example.crypto.data.model.Coin

object FakeData {
    val cryptoCoins = listOf(
        Coin("Bitcoin", "BTC", false, true, "coin"),
        Coin("Ethereum", "ETH", false, true, "token"),
        Coin("Ripple", "XRP", false, false, "coin"),
        Coin("Cardano", "ADA", true, true, "coin")
    )

    val filteredActiveCryptoCoins = listOf(
        Coin("Bitcoin", "BTC", false, true, "coin"),
        Coin("Ethereum", "ETH", false, true, "token"),
        Coin("Cardano", "ADA", true, true, "coin")
    )
}