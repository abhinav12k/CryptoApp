package com.example.crypto.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crypto.data.model.Coin
import com.example.crypto.ui.components.CoinDetailView

class CoinsAdapter : ListAdapter<Coin, CoinsAdapter.CoinViewHolder>(AdapterItemDiffCallback()) {

    class CoinViewHolder(private val coinDetailView: CoinDetailView) :
        RecyclerView.ViewHolder(coinDetailView) {
        fun updateView(data: Coin) {
            coinDetailView.updateView(data)
        }

        fun updateViewWithPayload(data: Coin, payload: Any) {
            if (payload is Coin) updateView(payload)
            else updateView(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder(CoinDetailView(parent.context))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.updateView(getItem(position))
    }

    private fun bindViewHolderWithPayload(
        model: Coin,
        holder: CoinViewHolder,
        payload: Any
    ) {
        holder.updateViewWithPayload(model, payload)
    }

    override fun onBindViewHolder(
        holder: CoinViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty() || payloads.getOrNull(0) == null) {
            onBindViewHolder(holder, position)
        } else {
            bindViewHolderWithPayload(getItem(position), holder, payloads[0])
        }
    }

}