package com.example.crypto.ui

import com.example.crypto.data.model.Coin

sealed interface CryptoViewState {
    data class CryptoData(val coins: List<Coin>, val isFromCache: Boolean = false) : CryptoViewState
    data class Error(val message: String?, val retryAllowed: Boolean = true) : CryptoViewState
    data object Loading : CryptoViewState
}
