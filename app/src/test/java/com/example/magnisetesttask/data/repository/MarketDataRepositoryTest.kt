package com.example.magnisetesttask.data.repository

import com.example.magnisetesttask.data.remote.retrofit.FintachartsApi
import com.example.magnisetesttask.data.remote.retrofit.dto.ActiveTick
import com.example.magnisetesttask.data.remote.retrofit.dto.DateRangeData
import com.example.magnisetesttask.data.remote.retrofit.dto.DateRangeResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.Dxfeed
import com.example.magnisetesttask.data.remote.retrofit.dto.Gics
import com.example.magnisetesttask.data.remote.retrofit.dto.InstrumentsData
import com.example.magnisetesttask.data.remote.retrofit.dto.InstrumentsResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.Mappings
import com.example.magnisetesttask.data.remote.retrofit.dto.Oanda
import com.example.magnisetesttask.data.remote.retrofit.dto.Paging
import com.example.magnisetesttask.data.remote.retrofit.dto.Profile
import com.example.magnisetesttask.data.remote.retrofit.dto.Simulation
import com.example.magnisetesttask.data.remote.web_socket.RealTimeMarketData
import com.example.magnisetesttask.data.remote.web_socket.dto.RealTimeData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MarketDataRepositoryTest {

    private var fintachartsApi = mock<FintachartsApi>()
    private var realTimeMarketData = mock<RealTimeMarketData>()
    private lateinit var repository: MarketDataRepositoryImpl

    @Before
    fun setUp() {
        repository = MarketDataRepositoryImpl(fintachartsApi, realTimeMarketData)
    }

    @After
    fun tearDown() {
        Mockito.reset(fintachartsApi, realTimeMarketData)
    }

    @Test
    fun `getSymbols should return mapped list of symbols`() = runTest {
        val provider = "TestProvider"
        val instrumentsResponse = InstrumentsResponse(
            data = listOf(
                InstrumentsData(
                    baseCurrency = "USD",
                    currency = "EUR",
                    description = "Test Instrument",
                    id = "1",
                    kind = "Test Kind",
                    mappings = Mappings(
                        activeTick = ActiveTick(1, "Exchange1", "Symbol1"),
                        dxfeed = Dxfeed(1, "Exchange2", "Symbol2"),
                        oanda = Oanda(1, "Exchange3", "Symbol3"),
                        simulation = Simulation(1, "Exchange4", "Symbol4")
                    ),
                    profile = Profile(gics = Gics(), name = "Test Profile"),
                    symbol = "AAPL",
                    tickSize = 0.01
                )
            ),
            paging = Paging(items = 1, page = 1, pages = 1)
        )

        whenever(fintachartsApi.getInstruments(provider)).thenReturn(instrumentsResponse)

        val symbols = repository.getSymbols(provider)

        assertEquals(1, symbols.size)
        assertEquals("1", symbols[0].id)
        assertEquals("AAPL", symbols[0].symbol)
    }

    @Test
    fun `getRealTimeMarketData should return mapped market data`() = runTest {
        val realTimeData =
            RealTimeData(price = 150.0, instrumentId = "1", timestamp = "2023-01-01T00:00:00Z")
        whenever(realTimeMarketData.getRealTimeMarketData()).thenReturn(flowOf(realTimeData))

        val marketDataList = repository.getRealTimeMarketData().toList()

        assertEquals(1, marketDataList.size)
        assertEquals(150.0, marketDataList[0].price, 0.01)
        assertEquals("1", marketDataList[0].instrumentId)
        assertEquals("2023-01-01T00:00:00Z", marketDataList[0].timestamp)
    }

    @Test
    fun `subscribeToMarketData should call subscribe on realTimeMarketData`() {
        val instrumentId = "1"

        repository.subscribeToMarketData(instrumentId)

        verify(realTimeMarketData).subscribe(instrumentId)
    }

    @Test
    fun `getHistoricalPrices should return mapped historical prices`() = runTest {
        val instrumentId = "1"
        val startDate = "2023-01-01"
        val provider = "TestProvider"
        val interval = 1
        val periodicity = "daily"

        val historicalPricesResponse = DateRangeResponse(
            data = listOf(
                DateRangeData(
                    close = 152.0,
                    high = 155.0,
                    low = 145.0,
                    open = 150.0,
                    timestamp = "2023-01-01T00:00:00Z",
                    volume = 1000
                )
            )
        )

        whenever(
            fintachartsApi.getDateRange(
                instrumentId = instrumentId,
                startDate = startDate,
                provider = provider,
                interval = interval,
                periodicity = periodicity
            )
        ).thenReturn(historicalPricesResponse)

        val historicalPrices =
            repository.getHistoricalPrices(instrumentId, startDate, provider, interval, periodicity)

        assertEquals(1, historicalPrices.size)
        assertEquals(152.0, historicalPrices[0].close, 0.01)
        assertEquals(155.0, historicalPrices[0].high, 0.01)
        assertEquals(145.0, historicalPrices[0].low, 0.01)
        assertEquals(150.0, historicalPrices[0].open, 0.01)
        assertEquals("2023-01-01T00:00:00Z", historicalPrices[0].timestamp)
        assertEquals(1000, historicalPrices[0].volume)
    }
}
