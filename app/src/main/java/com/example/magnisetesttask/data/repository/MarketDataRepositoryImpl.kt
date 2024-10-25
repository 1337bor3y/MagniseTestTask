package com.example.magnisetesttask.data.repository

import com.example.magnisetesttask.data.remote.retrofit.FintachartsApi
import com.example.magnisetesttask.data.remote.web_socket.RealTimeMarketData
import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.LiveMarketData
import com.example.magnisetesttask.domain.model.Symbol
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.map

class MarketDataRepositoryImpl(
    private val fintachartsApi: FintachartsApi,
    private val realTimeMarketData: RealTimeMarketData
) : MarketDataRepository {

    override suspend fun getSymbolsList(): List<Symbol> {
        val response = fintachartsApi.getInstruments(
            provider = "oanda",
            kind = "forex"
        )

        return response.data.map {
            Symbol(
                id = it.id,
                symbol = it.symbol
            )
        }
    }

    override suspend fun getMarketLiveData(instrumentId: String) =
        realTimeMarketData.getRealTimeMarketData(instrumentId).map {
            LiveMarketData(
                price = it.price,
                instrumentId = it.instrumentId,
                timestamp = it.timestamp
            )
        }

    override suspend fun getHistoricalPrices(
        instrumentId: String,
        startDate: String
    ): List<HistoricalPrice> {
        val response = fintachartsApi.getDateRange(
            instrumentId = instrumentId,
            startDate = startDate,
            provider = "simulation",
            interval = 1,
            periodicity = "minute"
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