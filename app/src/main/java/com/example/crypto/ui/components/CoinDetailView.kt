package com.example.crypto.ui.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.crypto.R
import com.example.crypto.data.model.Coin
import com.example.crypto.data.model.Coin.Companion.COIN_TYPE
import com.example.crypto.data.model.Coin.Companion.TOKEN_TYPE
import com.example.crypto.databinding.ViewCoinDetailBinding
import com.example.crypto.utils.dpToPx
import com.example.crypto.utils.isTrue
import com.google.android.material.card.MaterialCardView

class CoinDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = ViewCoinDetailBinding.inflate(LayoutInflater.from(context))

    init {
        setupView()
    }

    private fun setupView() {
        layoutParams =
            MarginLayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        cardElevation = 0f
        radius = 12.dpToPx(context)
        val horizontalPadding = 16.dpToPx(context).toInt()
        val verticalPadding = 24.dpToPx(context).toInt()
        setContentPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        addView(binding.root)
    }

    fun updateView(data: Coin) {
        binding.tvCoinName.text = data.name
        binding.tvCoinSymbol.text = data.symbol
        binding.ivCoinState.loadDrawable(getCoinState(data))
        binding.ivIsNew.loadDrawable(getNewDrawable(data.isNew.isTrue()))
        if (!data.isActive.isTrue()) {
            setCardBackgroundColor(Color.parseColor("#F5F7F9"))
        } else {
            setCardBackgroundColor(Color.WHITE)
        }
    }

    private fun getCoinState(data: Coin): Drawable? {
        return when (data.type) {
            TOKEN_TYPE -> {
                val id =
                    if (data.isActive.isTrue()) R.drawable.ic_token_active else R.drawable.ic_token_inactive
                ContextCompat.getDrawable(context, id)
            }

            COIN_TYPE -> {
                val id =
                    if (data.isActive.isTrue()) R.drawable.ic_coin_active else R.drawable.ic_coin_inactive
                ContextCompat.getDrawable(context, id)
            }

            else -> null
        }
    }

    private fun getNewDrawable(isNew: Boolean): Drawable? {
        return if (isNew) ContextCompat.getDrawable(context, R.drawable.ic_new) else null
    }

    private fun ImageView.loadDrawable(drawable: Drawable?) {
        if (drawable == null) {
            isVisible = false
            return
        }

        isVisible = true
        setImageDrawable(drawable)
    }

}