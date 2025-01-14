package com.example.magnisetesttask.domain.repository

import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.MarketData
import com.example.magnisetesttask.domain.model.Symbol
import kotlinx.coroutines.flow.Flow

interface MarketDataRepository {

    suspend fun getSymbols(provider: String): List<Symbol>

    fun getRealTimeMarketData(): Flow<MarketData>

    fun subscribeToMarketData(instrumentId: String)

    suspend fun getHistoricalPrices(
        instrumentId: String,
        startDate: String,
        provider: String,
        interval: Int,
        periodicity: String
    ): List<HistoricalPrice>
}