package com.example.crypto.ui.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.widget.doAfterTextChanged
import com.example.crypto.R
import com.example.crypto.databinding.ViewSearchBinding
import com.example.crypto.utils.dpToPx
import com.example.crypto.utils.showKeyboard
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Runnable

class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = ViewSearchBinding.inflate(LayoutInflater.from(context))
    private var afterTextChangedListener: ((input: String) -> Unit)? = null
    private var delayMillis: Long = 400
    private val debounceHandler: Handler = Handler(Looper.getMainLooper())

    init {
        setupView()
        setupViewAttributes(attrs)
        setupSearchTextChangeListener()
    }

    private fun setupView() {
        radius = 20.dpToPx(context)
        val horizontalPadding = 12.dpToPx(context).toInt()
        val verticalPadding = 10.dpToPx(context).toInt()
        setContentPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        addView(binding.root)
        setOnClickListener { binding.etSearch.showKeyboard() }
        foreground = null
    }

    private fun setupViewAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SearchBar,
            0, 0
        ).apply {
            try {
                binding.etSearch.hint = getString(R.styleable.SearchBar_hint)
                binding.etSearch.setText(getString(R.styleable.SearchBar_text))
            } finally {
                recycle()
            }
        }
    }

    private fun setupSearchTextChangeListener() {
        binding.etSearch.doAfterTextChanged { text ->
            debounceHandler.postDelayed({
                afterTextChangedListener?.invoke(text.toString().trim())
            }, delayMillis)
        }
    }

    fun setHint(hintText: String) {
        binding.etSearch.hint = hintText
    }

    fun getText() = binding.etSearch.text.toString()

    fun addAfterTextChangedListener(afterTextChangedListener: (input: String) -> Unit) {
        this.afterTextChangedListener = afterTextChangedListener
    }

    private class DebounceRunnable(
        private val input: String,
        private val debounceCallback: ((input: String) -> Unit)?
    ) : Runnable {
        override fun run() {
            debounceCallback?.invoke(input)
        }
    }
}