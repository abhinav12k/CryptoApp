package com.example.crypto.data.model

import com.google.gson.annotations.SerializedName

data class Coin(
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("is_new")
    val isNew: Boolean?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("type")
    val type: String?
) {
    companion object {
        const val COIN_TYPE = "coin"
        const val TOKEN_TYPE = "token"
    }
}
