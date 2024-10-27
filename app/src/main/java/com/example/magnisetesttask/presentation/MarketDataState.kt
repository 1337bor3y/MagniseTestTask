package com.example.magnisetesttask.presentation

import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.Symbol

data class MarketDataState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val isGraphLoading: Boolean = false,
    val symbols: List<Symbol> = emptyList(),
    val symbolId: String = "",
    val selectedSymbol: String = "",
    val price: String = "",
    val time: String = "",
    val historicalPrices: List<HistoricalPrice> = emptyList()
)