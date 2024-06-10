package com.example.crypto.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("coins")
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val name: String?,
    val symbol: String?,
    val isNew: Boolean?,
    val isActive: Boolean?,
    val type: String?
)