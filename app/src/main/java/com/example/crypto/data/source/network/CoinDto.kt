package com.example.crypto.data.source.network

import com.google.gson.annotations.SerializedName

data class CoinDto(
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
)