package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetHistoricalPricesUseCaseTest {

    private var marketDataRepository = mock<MarketDataRepository>()
    private lateinit var getHistoricalPricesUseCase: GetHistoricalPricesUseCase

    @Before
    fun setUp() {
        getHistoricalPricesUseCase = GetHistoricalPricesUseCase(marketDataRepository)
    }

    @After
    fun tearDown() {
        Mockito.reset(marketDataRepository)
    }

    @Test
    fun `invoke should emit loading and then success resource with historical prices`() = runTest {
        val symbolId = "1"
        val startDate = "2023-01-01"
        val provider = "TestProvider"
        val interval = 1
        val periodicity = "daily"
        val expectedPrices = listOf(
            HistoricalPrice(
                close = 152.0,
                high = 155.0,
                low = 145.0,
                open = 150.0,
                timestamp = "2023-01-01T00:00:00Z",
                volume = 1000
            )
        )

        whenever(
            marketDataRepository.getHistoricalPrices(
                symbolId,
                startDate,
                provider,
                interval,
                periodicity
            )
        ).thenReturn(expectedPrices)

        val flow =
            getHistoricalPricesUseCase(symbolId, startDate, provider, interval, periodicity)

        flow.collect { resource ->
            when (resource) {
                is Resource.Loading -> assertTrue(true)
                is Resource.Success -> {
                    assertEquals(expectedPrices, resource.data)
                }
                is Resource.Error -> assertTrue(false)
            }
        }
    }

    @Test
    fun `invoke should emit loading and then error resource when an exception occurs`() = runTest {
        val symbolId = "1"
        val startDate = "2023-01-01"
        val provider = "TestProvider"
        val interval = 1
        val periodicity = "daily"

        whenever(
            marketDataRepository.getHistoricalPrices(
                symbolId,
                startDate,
                provider,
                interval,
                periodicity
            )
        ).thenThrow(RuntimeException("Network Error"))

        val flow =
            getHistoricalPricesUseCase(symbolId, startDate, provider, interval, periodicity)

        flow.collect { resource ->
            when (resource) {
                is Resource.Loading -> assertTrue(true)
                is Resource.Success -> assertTrue(false)
                is Resource.Error -> {
                    assertEquals("Network Error", resource.message)
                }
            }
        }
    }
}