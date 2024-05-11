package com.example.crypto.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.crypto.FakeData
import com.example.crypto.FakeData.cryptoCoins
import com.example.crypto.data.CryptoRepository
import com.example.crypto.getOrAwaitValue
import com.example.crypto.utils.ErrorBody
import com.example.crypto.utils.Result
import com.example.crypto.utils.UNABLE_TO_CONNECT_TO_INTERNET
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CryptoViewModelTest {

    @Mock
    lateinit var cryptoRepository: CryptoRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var sut: CryptoViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        sut = CryptoViewModel(cryptoRepository)
    }

    @Test
    fun testFetchCryptoCoins_returnSuccess() = runTest {
        Mockito.`when`(cryptoRepository.fetchCryptoCoins()).thenReturn(Result.Success(cryptoCoins))

        sut.fetchCryptoCoins()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.viewState.getOrAwaitValue()
        assertThat((result as CryptoViewState.CryptoData).coins).isEqualTo(cryptoCoins)
        val isLoading = sut.isLoading.getOrAwaitValue()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun testFetchCryptoCoins_returnSuccessWithNoContent() = runTest {
        Mockito.`when`(cryptoRepository.fetchCryptoCoins()).thenReturn(Result.SuccessWithNoResult)

        sut.fetchCryptoCoins()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.viewState.getOrAwaitValue()
        assertThat((result as CryptoViewState.Error).message).isEqualTo("No Data Found!")
        val isLoading = sut.isLoading.getOrAwaitValue()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun testFetchCryptoCoins_returnFailure() = runTest {
        Mockito.`when`(cryptoRepository.fetchCryptoCoins())
            .thenReturn(Result.Failure(ErrorBody(UNABLE_TO_CONNECT_TO_INTERNET)))

        sut.fetchCryptoCoins()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.viewState.getOrAwaitValue()
        assertThat((result as CryptoViewState.Error).message)
            .isEqualTo(UNABLE_TO_CONNECT_TO_INTERNET)
        val isLoading = sut.isLoading.getOrAwaitValue()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun testApplyFilter_anySearchText_notEmptySelectedFilter_withEmptyCoinList_returnError() {
        sut.applyFilters("test", listOf(1))
        testDispatcher.scheduler.advanceUntilIdle()
        val viewState = sut.viewState.getOrAwaitValue()
        assertThat((viewState as CryptoViewState.Error).message).isEqualTo("Please wait fetching latest coins...")
    }

    @Test
    fun testApplyFilter_emptySearchText_emptySelectedFilterList_withNotEmptyCoinList_returnCompleteCoinList() =
        runTest {
            Mockito.`when`(cryptoRepository.fetchCryptoCoins())
                .thenReturn(Result.Success(FakeData.cryptoCoins))

            sut.fetchCryptoCoins()
            sut.applyFilters("", emptyList())
            testDispatcher.scheduler.advanceUntilIdle()
            val viewState = sut.viewState.getOrAwaitValue()

            assertThat((viewState as CryptoViewState.CryptoData).coins).isEqualTo(cryptoCoins)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

}