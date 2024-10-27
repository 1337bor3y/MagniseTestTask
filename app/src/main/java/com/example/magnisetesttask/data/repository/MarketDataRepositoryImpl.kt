package com.example.magnisetesttask.data.repository

import com.example.magnisetesttask.data.remote.retrofit.FintachartsApi
import com.example.magnisetesttask.data.remote.web_socket.RealTimeMarketData
import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.MarketData
import com.example.magnisetesttask.domain.model.Symbol
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarketDataRepositoryImpl @Inject constructor(
    private val fintachartsApi: FintachartsApi,
    private val realTimeMarketData: RealTimeMarketData
) : MarketDataRepository {

    override suspend fun getSymbols(provider: String): List<Symbol> {
        val response = fintachartsApi.getInstruments(provider)

        return response.data.map {
            Symbol(
                id = it.id,
                symbol = it.symbol
            )
        }
    }

    override fun getRealTimeMarketData() =
        realTimeMarketData.getRealTimeMarketData().map {
            MarketData(
                price = it.price,
                instrumentId = it.instrumentId,
                timestamp = it.timestamp
            )
        }

    override fun subscribeToMarketData(instrumentId: String) =
        realTimeMarketData.subscribe(instrumentId)

    override suspend fun getHistoricalPrices(
        instrumentId: String,
        startDate: String,
        provider: String,
        interval: Int,
        periodicity: String
    ): List<HistoricalPrice> {
        val response = fintachartsApi.getDateRange(
            instrumentId = instrumentId,
            startDate = startDate,
            provider = provider,
            interval = interval,
            periodicity = periodicity
        )

        return response.data.map {
            HistoricalPrice(
                timestamp = it.timestamp,
                volume = it.volume,
                high = it.high,
                open = it.open,
                low = it.low,
                close = it.close
            )
        }
    }
}