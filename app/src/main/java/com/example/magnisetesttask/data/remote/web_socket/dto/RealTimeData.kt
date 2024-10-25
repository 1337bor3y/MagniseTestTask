package com.example.magnisetesttask.data.remote.web_socket.dto

data class RealTimeData(
    val instrumentId: String,
    val timestamp: String,
    val price: Double,
)