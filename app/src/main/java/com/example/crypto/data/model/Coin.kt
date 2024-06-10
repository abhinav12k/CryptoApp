package com.example.crypto.data.model

data class Coin(
    val name: String?,
    val symbol: String?,
    val isNew: Boolean?,
    val isActive: Boolean?,
    val type: String?
) {
    companion object {
        const val COIN_TYPE = "coin"
        const val TOKEN_TYPE = "token"
    }
}
