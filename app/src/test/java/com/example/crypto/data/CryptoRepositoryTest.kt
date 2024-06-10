package com.example.crypto.data

import com.example.crypto.FakeData
import com.example.crypto.data.source.network.CryptoApi
import com.example.crypto.utils.Result
import com.example.crypto.utils.UNABLE_TO_CONNECT_TO_INTERNET
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.IOException

class CryptoRepositoryTest {

    @Mock
    lateinit var cryptoApi: CryptoApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFetchCryptoCoins_returnSuccessWithNotEmptyList() = runTest {
        Mockito.`when`(cryptoApi.getCoinData()).thenReturn(Response.success(FakeData.cryptoCoins))

        val sut = CryptoRepository(cryptoApi)

        val result = sut.fetchCryptoCoins()

        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isEqualTo(FakeData.cryptoCoins)
    }

    @Test
    fun testFetchCryptoCoins_returnSuccessWithEmptyList() = runTest {
        Mockito.`when`(cryptoApi.getCoinData()).thenReturn(Response.success(emptyList()))

        val sut = CryptoRepository(cryptoApi)

        val result = sut.fetchCryptoCoins()

        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data.size).isEqualTo(0)
    }

    @Test
    fun testFetchCryptoCoins_returnError() = runTest {
        Mockito.`when`(cryptoApi.getCoinData()).thenReturn(
            Response.error(
                401,
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{\"message\":\"Unauthorized\"}"
                )
            )
        )

        val sut = CryptoRepository(cryptoApi)

        val result = sut.fetchCryptoCoins()

        assertThat(result is Result.Failure).isTrue()
        assertThat((result as Result.Failure).errorBody.code).isEqualTo(401)
        assertThat(result.errorBody.message).isEqualTo("Unauthorized")
        assertThat(result.errorBody.rawMessage).isEqualTo("{\"message\":\"Unauthorized\"}")
    }

    @Test
    fun testFetchCryptoCoins_returnSuccessWithNullResult() = runTest {
        Mockito.`when`(cryptoApi.getCoinData()).thenReturn(Response.success(null))

        val sut = CryptoRepository(cryptoApi)

        val result = sut.fetchCryptoCoins()

        assertThat(result is Result.SuccessWithNoResult).isTrue()
    }

    @Test(expected = RuntimeException::class)
    fun testFetchCryptoCoins_returnErrorWithIOException() = runTest {
        Mockito.`when`(cryptoApi.getCoinData()).thenThrow(IOException())

        val sut = CryptoRepository(cryptoApi)

        val result = sut.fetchCryptoCoins()

        assertThat(result is Result.Failure).isTrue()
        assertThat((result as Result.Failure).errorBody.message).isEqualTo(
            UNABLE_TO_CONNECT_TO_INTERNET
        )
    }


    @Test
    fun testFetchCryptoCoins_returnErrorWithJsonSyntaxException() = runTest {
        Mockito.`when`(cryptoApi.getCoinData()).thenReturn(
            Response.error(
                401,
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{{\"message\":\"Unauthorized\"}"
                )
            )
        )

        val sut = CryptoRepository(cryptoApi)

        val result = sut.fetchCryptoCoins()

        assertThat(result is Result.Failure).isTrue()
        assertThat((result as Result.Failure).errorBody.message).isEqualTo("Error while parsing json")
    }
}