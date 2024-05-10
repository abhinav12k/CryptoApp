package com.example.crypto.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.crypto.data.model.Coin

class AdapterItemDiffCallback : DiffUtil.ItemCallback<Coin>() {

    override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Coin, newItem: Coin): Any {
        return newItem
    }

}
