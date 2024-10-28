package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.model.MarketData
import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GetRealTimeDataUseCaseTest {

    private var marketDataRepository = mock<MarketDataRepository>()
    private lateinit var getRealTimeDataUseCase: GetRealTimeDataUseCase

    @Before
    fun setUp() {
        getRealTimeDataUseCase = GetRealTimeDataUseCase(marketDataRepository)
    }

    @After
    fun tearDown() {
        Mockito.reset(marketDataRepository)
    }

    @Test
    fun `invoke should emit loading and then success resource with market data`() = runTest {
        val expectedMarketData = MarketData(
            instrumentId = "1",
            timestamp = "2024-10-28T10:00:00Z",
            price = 150.0
        )

        whenever(marketDataRepository.getRealTimeMarketData()).thenReturn(
            flowOf(
                expectedMarketData
            )
        )

        val flow = getRealTimeDataUseCase()

        flow.collect { resource ->
            when (resource) {
                is Resource.Loading -> assertTrue(true)
                is Resource.Success -> {
                    assertEquals(
                        expectedMarketData.copy(
                            timestamp = formatTimestampToLocal(expectedMarketData.timestamp)
                        ), resource.data!!.toList()[0]
                    )
                }
                is Resource.Error -> assertTrue(false)
            }
        }
    }

    private fun formatTimestampToLocal(timestamp: String): String {
        val zonedDateTime = ZonedDateTime.parse(timestamp)
        val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMM d, h:mm a")
        return localDateTime.format(formatter)
    }

    @Test
    fun `invoke should emit loading and then error resource when an exception occurs`() = runTest {
        whenever(marketDataRepository.getRealTimeMarketData()).thenThrow(RuntimeException("Network Error"))

        val flow = getRealTimeDataUseCase()

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