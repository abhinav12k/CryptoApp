package com.example.crypto.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto.CryptoApp
import com.example.crypto.data.CryptoRepositoryImpl
import com.example.crypto.data.remote.CryptoApi
import com.example.crypto.databinding.ActivityCryptoBinding
import com.example.crypto.utils.MarginItemDecoration
import com.example.crypto.utils.getViewModel

class CryptoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCryptoBinding
    private val viewModel: CryptoViewModel by lazy {
        getViewModel {
            CryptoViewModel(
                CryptoRepositoryImpl((application as CryptoApp).retrofit.create(CryptoApi::class.java))
            )
        }
    }

    private val adapter by lazy { CoinsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSearchBar()
        setupRecyclerView()
        setupLiveDataObservers()
        if (viewModel.refetchCoinsList) viewModel.fetchCryptoCoins()
        binding.flFilter.setOnClickListener { launchFilterBottomsheet() }
    }

    private fun launchFilterBottomsheet() {
        val fragment =
            supportFragmentManager.findFragmentByTag(FilterBottomsheet::class.java.simpleName) as? FilterBottomsheet
        if (fragment?.isVisible == true) fragment.dismiss()
        val filterBottomsheet = FilterBottomsheet.getInstance(viewModel.filters.toTypedArray())
        filterBottomsheet.attachFilterListener {
            viewModel.applyFilters(
                binding.searchBar.getText(),
                it
            )
        }
        filterBottomsheet.show(supportFragmentManager, FilterBottomsheet::class.java.simpleName)
    }

    private fun setupSearchBar() {
        binding.searchBar.addAfterTextChangedListener {
            viewModel.performSearch(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rvCoins.apply {
            layoutManager =
                LinearLayoutManager(this@CryptoActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@CryptoActivity.adapter
            addItemDecoration(MarginItemDecoration(16))
        }
    }

    private fun setupLiveDataObservers() {
        viewModel.viewState.observe(this) {
            handleViewState(it)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }


    private fun handleViewState(viewState: CryptoViewState) {
        when (viewState) {
            is CryptoViewState.CryptoData -> {
                binding.errorLayout.isVisible = false
                binding.rvCoins.isVisible = true

                adapter.submitList(viewState.coins)
                binding.rvCoins.smoothScrollToPosition(0)
            }

            is CryptoViewState.Error -> {
                binding.errorLayout.isVisible = true
                binding.rvCoins.isVisible = false

                binding.errorText.text = viewState.message
                binding.errorBtn.isVisible = viewState.retryAllowed
                binding.errorBtn.setOnClickListener {
                    viewModel.fetchCryptoCoins()
                    binding.errorLayout.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.refetchCoinsList = !isChangingConfigurations
    }
}