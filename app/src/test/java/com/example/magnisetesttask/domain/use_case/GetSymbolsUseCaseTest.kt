package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.model.Symbol
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetSymbolsUseCaseTest {

    private var marketDataRepository = mock<MarketDataRepository>()
    private lateinit var getSymbolsUseCase: GetSymbolsUseCase

    @Before
    fun setUp() {
        getSymbolsUseCase = GetSymbolsUseCase(marketDataRepository)
    }

    @After
    fun tearDown() {
        Mockito.reset(marketDataRepository)
    }

    @Test
    fun `invoke should emit loading and then success resource with symbols`() = runBlocking {
        val provider = "test_provider"
        val expectedSymbols = listOf(
            Symbol(id = "1", symbol = "AAPL"),
            Symbol(id = "2", symbol = "GOOGL")
        )

        whenever(marketDataRepository.getSymbols(provider)).thenReturn(expectedSymbols)

        val flow = getSymbolsUseCase(provider)

        flow.collect { resource ->
            when (resource) {
                is Resource.Loading -> assertTrue(true)
                is Resource.Success -> {
                    assertEquals(expectedSymbols, resource.data)
                }
                is Resource.Error -> assertTrue(false)
            }
        }
    }

    @Test
    fun `invoke should emit loading and then error resource when an exception occurs`() = runBlocking {
        val provider = "test_provider"
        whenever(marketDataRepository.getSymbols(provider)).thenThrow(RuntimeException("Network Error"))

        val flow = getSymbolsUseCase(provider)

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
