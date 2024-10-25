package com.example.magnisetesttask.domain.model

data class LiveMarketData(
    val instrumentId: String,
    val timestamp: String,
    val price: Double,
)
