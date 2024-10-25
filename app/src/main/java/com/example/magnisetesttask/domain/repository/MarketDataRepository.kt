package com.example.magnisetesttask.domain.repository

import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.MarketData
import com.example.magnisetesttask.domain.model.Symbol
import kotlinx.coroutines.flow.Flow

interface MarketDataRepository {

    suspend fun getSymbols(): List<Symbol>

    suspend fun getMarketData(instrumentId: String): Flow<MarketData>

    suspend fun getHistoricalPrices(instrumentId: String, startDate: String): List<HistoricalPrice>
}