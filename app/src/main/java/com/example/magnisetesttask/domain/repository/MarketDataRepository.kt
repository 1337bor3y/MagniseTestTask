package com.example.magnisetesttask.domain.repository

import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.LiveMarketData
import com.example.magnisetesttask.domain.model.Symbol
import kotlinx.coroutines.flow.Flow

interface MarketDataRepository {

    suspend fun getSymbolsList(): List<Symbol>

    suspend fun getMarketLiveData(instrumentId: String): Flow<LiveMarketData>

    suspend fun getHistoricalPrices(instrumentId: String, startDate: String): List<HistoricalPrice>
}