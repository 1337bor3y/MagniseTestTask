package com.example.magnisetesttask.presentation

import app.cash.turbine.test
import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.MarketData
import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.model.Symbol
import com.example.magnisetesttask.domain.use_case.GetHistoricalPricesUseCase
import com.example.magnisetesttask.domain.use_case.GetRealTimeDataUseCase
import com.example.magnisetesttask.domain.use_case.GetSymbolsUseCase
import com.example.magnisetesttask.domain.use_case.SubscribeToMarketDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MarketDataViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    private var getSymbolsUseCase = mock<GetSymbolsUseCase>()
    private var getRealTimeDataUseCase = mock<GetRealTimeDataUseCase>()
    private var subscribeToMarketDataUseCase = mock<SubscribeToMarketDataUseCase>()
    private var getHistoricalPricesUseCase = mock<GetHistoricalPricesUseCase>()
    private lateinit var viewModel: MarketDataViewModel

    private val symbols = listOf(Symbol("1", "BTC"))
    private val marketDataFlow = flowOf(
        MarketData(
            "AAPL",
            "2023-08-07T00:00:00Z",
            150.0
        )
    )

    @Before
    fun setUp() {
        whenever(getSymbolsUseCase("simulation")).thenReturn(flowOf(Resource.Success(symbols)))
        whenever(getRealTimeDataUseCase()).thenReturn(flowOf(Resource.Success(marketDataFlow)))

        viewModel = MarketDataViewModel(
            getSymbolsUseCase,
            getRealTimeDataUseCase,
            subscribeToMarketDataUseCase,
            getHistoricalPricesUseCase
        )
    }

    @After
    fun tearDown() {
        Mockito.reset(
            getSymbolsUseCase,
            getRealTimeDataUseCase,
            subscribeToMarketDataUseCase,
            getHistoricalPricesUseCase
        )
    }

    @Test
    fun `init should update state with symbols`() = runTest {
        viewModel.state.test {
            val item = awaitItem()
            assertEquals(symbols, item.symbols)
            assertEquals("150.0", item.price)
            assertEquals(null, item.error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setUpConnection should update state with symbols and data`() = runTest {
        val symbols = listOf(Symbol("1", "BTC"))
        val realTimeData = MarketData("1", "2023-08-07T00:00:00Z", 50000.0)
        val historicalPrices = listOf(
            HistoricalPrice(50000.0, 50500.0, 49500.0, 50000.0, "2023-08-07T00:00:00Z", 100)
        )
        whenever(getSymbolsUseCase("simulation")).thenReturn(flowOf(Resource.Success(symbols)))
        whenever(getRealTimeDataUseCase()).thenReturn(flowOf(Resource.Success(flowOf(realTimeData))))
        whenever(
            getHistoricalPricesUseCase(
                symbolId = viewModel.state.value.symbolId,
                provider = "simulation",
                startDate = "2023-08-07",
                interval = 1,
                periodicity = "minute"
            )
        ).thenReturn(flowOf(Resource.Success(historicalPrices)))

        viewModel.onEvent(MarketDataEvent.RetryConnection)

        viewModel.state.test {
            val item = awaitItem()
            assertEquals(symbols, item.symbols)
            assertEquals("50000.0", item.price)
            assertEquals(null, item.error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `chooseSymbol should update selected symbol and retrieve historical data`() = runTest {
        val symbolId = "1"
        val symbolName = "BTC"
        val historicalPrices = listOf(
            HistoricalPrice(50000.0, 50500.0, 49500.0, 50000.0, "2023-08-07T00:00:00Z", 100)
        )
        whenever(subscribeToMarketDataUseCase(any())).then { }
        whenever(
            getHistoricalPricesUseCase(
                symbolId = symbolId,
                provider = "simulation",
                startDate = "2023-08-07",
                interval = 1,
                periodicity = "minute"
            )
        ).thenReturn(flowOf(Resource.Success(historicalPrices)))

        viewModel.onEvent(MarketDataEvent.ChooseSymbol(symbolId, symbolName))

        viewModel.state.test {
            val item = awaitItem()
            assertEquals(symbolName, item.selectedSymbol)
            assertEquals(historicalPrices, item.historicalPrices)
            assertEquals(false, item.isGraphLoading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHistoricalData should handle error state correctly`() = runTest {
        val symbolId = "1"
        val errorMessage = "Network Error"
        whenever(
            getHistoricalPricesUseCase(
                symbolId = symbolId,
                provider = "simulation",
                startDate = "2023-08-07",
                interval = 1,
                periodicity = "minute"
            )
        ).thenReturn(flowOf(Resource.Error(errorMessage)))

        viewModel.onEvent(MarketDataEvent.ChooseSymbol(symbolId, "Bitcoin"))

        viewModel.state.test {
            val item = awaitItem()
            assertEquals(errorMessage, item.error)
            assertEquals(false, item.isGraphLoading)
            cancelAndConsumeRemainingEvents()
        }
    }
}
