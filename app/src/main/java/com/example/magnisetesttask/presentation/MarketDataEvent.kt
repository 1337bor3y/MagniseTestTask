package com.example.magnisetesttask.presentation

sealed interface MarketDataEvent {
    data class ChooseSymbol(val symbolId: String, val symbolName: String) : MarketDataEvent
    data object RetryConnection : MarketDataEvent
}