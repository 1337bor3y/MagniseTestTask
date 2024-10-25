package com.example.magnisetesttask.data.remote.web_socket.dto

data class RealTimeData(
    val type: String,
    val instrumentId: String,
    val provider: String,
    val last: Last
)

data class Last(
    val change: Double,
    val changePct: Double,
    val price: Double,
    val timestamp: String,
    val volume: Int
)