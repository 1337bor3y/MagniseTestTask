package com.example.magnisetesttask.domain.model

data class HistoricalPrice(
    val close: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val timestamp: String,
    val volume: Int
)
