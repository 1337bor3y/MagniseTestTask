package com.example.magnisetesttask.presentation

import com.example.magnisetesttask.domain.model.Symbol

data class MarketDataState(
    val symbols: List<Symbol> = emptyList(),
    val symbolId: String = "",
    val selectedSymbol: String = "",
    val price: String = "",
    val time: String = ""
)