package com.example.crypto.data.model

enum class FilterTypes(val filterName: String, val filterId: Int) {
    ACTIVE_COINS("Active Coins", 1),
    INACTIVE_COINS("Inactive Coins", 2),
    ONLY_COINS("Only Coins", 3),
    NEW_COINS("New Coins", 4),
    ONLY_TOKENS("Only Tokens", 5)
}