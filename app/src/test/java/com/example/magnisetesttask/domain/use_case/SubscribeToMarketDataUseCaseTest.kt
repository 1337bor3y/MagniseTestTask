package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SubscribeToMarketDataUseCaseTest {

    private var marketDataRepository = mock<MarketDataRepository>()
    private lateinit var subscribeToMarketDataUseCase: SubscribeToMarketDataUseCase

    @Before
    fun setUp() {
        subscribeToMarketDataUseCase = SubscribeToMarketDataUseCase(marketDataRepository)
    }

    @After
    fun tearDown() {
        Mockito.reset(marketDataRepository)
    }

    @Test
    fun `invoke should call subscribeToMarketData with correct symbolId`() = runBlocking {
        val symbolId = "1"

        subscribeToMarketDataUseCase(symbolId)

        verify(marketDataRepository).subscribeToMarketData(symbolId)
    }
}
