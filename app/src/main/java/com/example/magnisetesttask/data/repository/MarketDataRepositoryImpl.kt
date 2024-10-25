package com.example.magnisetesttask.data.repository

import android.util.Log
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

    override suspend fun getSymbols(): List<Symbol> {
        try {
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
        } catch (e: Exception) {
            e.message?.let { Log.d("getSymbols", it) }
            return emptyList()
        }
    }

    override suspend fun getMarketData(instrumentId: String) =
        realTimeMarketData.getRealTimeMarketData(instrumentId).map {
            MarketData(
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