package com.example.crypto.ui

import com.example.crypto.data.model.Coin

sealed class CryptoViewState {
    data class CryptoData(val coins: List<Coin>) : CryptoViewState()
    data class Error(val message: String?, val retryAllowed: Boolean = true) : CryptoViewState()
}
