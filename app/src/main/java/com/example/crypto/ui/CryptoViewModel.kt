package com.example.crypto.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.data.model.Coin
import com.example.crypto.data.model.Coin.Companion.COIN_TYPE
import com.example.crypto.data.model.Coin.Companion.TOKEN_TYPE
import com.example.crypto.data.model.Filter
import com.example.crypto.data.model.FilterTypes
import com.example.crypto.domain.CryptoRepository
import com.example.crypto.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CryptoViewModel(private val repository: CryptoRepository) : ViewModel() {

    var refetchCoinsList: Boolean = true
        set(value) {
            field = if (originalCoinList == null) true else value
        }

    private val _viewState = MutableLiveData<CryptoViewState>()
    val viewState: LiveData<CryptoViewState> = _viewState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var originalCoinList: List<Coin>? = null
    val filters: List<Filter> get() = updatedFilters
    private var updatedFilters: MutableList<Filter> =
        FilterTypes.entries.map { Filter(it.filterName, false, it.filterId) }.toMutableList()

    fun fetchCryptoCoins() {
        viewModelScope.launch {
            fetch {
                Log.d("Crypto", "Fetching coins from remote")
                when (val res = repository.fetchCryptoCoins()) {
                    is Result.Success -> {
                        originalCoinList = res.data
                        _viewState.value = CryptoViewState.CryptoData(res.data)
                    }

                    is Result.SuccessWithNoResult -> {
                        _viewState.value = CryptoViewState.Error("No Data Found!")
                    }

                    is Result.Failure -> {
                        _viewState.value = CryptoViewState.Error(res.errorBody.message)
                    }
                }
            }
        }
    }

    fun applyFilters(searchText: String, selectedFilters: List<Int>?) {
        viewModelScope.launch {
            val coinList = originalCoinList
            if (coinList == null) {
                _viewState.value = CryptoViewState.Error("Please wait fetching latest coins...")
                return@launch
            }

            fetch {
                if (selectedFilters == null) {
                    _viewState.value = if (searchText.isEmpty()) {
                        CryptoViewState.CryptoData(coinList)
                    } else {
                        CryptoViewState.CryptoData(findCoins(coinList, searchText))
                    }
                } else {
                    _viewState.value = CryptoViewState.CryptoData(
                        findCoinsWithFilters(
                            filterCoinList(
                                coinList, selectedFilters
                            ), searchText
                        )
                    )
                }
            }
        }
    }

    private suspend fun filterCoinList(coinList: List<Coin>, selectedFilters: List<Int>) =
        withContext(Dispatchers.Default) {
            var includeActiveCoins = false
            var includeInActiveCoins = false
            var includeOnlyCoins = false
            var includeNewCoins = false
            var includeOnlyTokens = false

            selectedFilters.forEach {
                when (it) {
                    FilterTypes.ACTIVE_COINS.filterId -> includeActiveCoins = true
                    FilterTypes.INACTIVE_COINS.filterId -> includeInActiveCoins = true
                    FilterTypes.NEW_COINS.filterId -> includeNewCoins = true
                    FilterTypes.ONLY_COINS.filterId -> includeOnlyCoins = true
                    FilterTypes.ONLY_TOKENS.filterId -> includeOnlyTokens = true
                }
            }

            updatedFilters.forEach { filter ->
                filter.isSelected = filter.filterId in selectedFilters
            }

            val noFilterSelected = selectedFilters.isEmpty()
            if (noFilterSelected) return@withContext coinList

            fun validCoinOverFilter(coin: Coin): Boolean {
                return if (includeActiveCoins && includeInActiveCoins && includeNewCoins) coin.isNew == true
                else if (includeActiveCoins && includeNewCoins) coin.isActive == true && coin.isNew == true
                else if (includeInActiveCoins && includeNewCoins) coin.isActive == false && coin.isNew == true
                else if (includeActiveCoins && includeInActiveCoins) true
                else if (includeActiveCoins) coin.isActive == true
                else if (includeInActiveCoins) coin.isActive == false
                else if (includeNewCoins) coin.isNew == true
                else if (includeOnlyCoins && includeOnlyTokens) true
                else if (includeOnlyCoins) coin.type == COIN_TYPE
                else if (includeOnlyTokens) coin.type == TOKEN_TYPE
                else false
            }

            return@withContext coinList.filter {
                if (includeOnlyTokens && includeOnlyCoins) validCoinOverFilter(it)
                else if (includeOnlyCoins) it.type == COIN_TYPE && validCoinOverFilter(
                    it
                )
                else if (includeOnlyTokens) it.type == TOKEN_TYPE && validCoinOverFilter(
                    it
                )
                else validCoinOverFilter(it)
            }
        }

    private var searchJob: Job? = null

    fun performSearch(inputStr: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            fetch {
                Log.d("Crypto", "Starting search - $inputStr")
                val coinList = originalCoinList
                _viewState.value = if (coinList.isNullOrEmpty()) {
                    CryptoViewState.Error("No Coins found!")
                } else {
                    CryptoViewState.CryptoData(findCoinsWithFilters(coinList, inputStr))
                }
            }
        }
    }

    private suspend fun findCoinsWithFilters(coinList: List<Coin>, inputStr: String) = findCoins(
        filterCoinList(coinList,
            filters.filter { it.isSelected }.map { it.filterId }
        ),
        inputStr
    )

    private suspend fun findCoins(coinList: List<Coin>, inputStr: String) =
        withContext(Dispatchers.Default) {
            return@withContext coinList.filter {
                (it.name?.startsWith(inputStr, true) == true || it.symbol?.startsWith(
                    inputStr, true
                ) == true)
            }
        }

    private inline fun fetch(block: () -> Unit) {
        _isLoading.value = true
        block()
        _isLoading.value = false
    }

}