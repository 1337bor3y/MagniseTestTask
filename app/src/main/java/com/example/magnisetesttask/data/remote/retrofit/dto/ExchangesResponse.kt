package com.example.magnisetesttask.data.remote.retrofit.dto

data class ExchangesResponse(
    val data: ExchangesData
)

data class ExchangesData(
    val activeTick: List<String>,
    val alpaca: List<String>,
    val cryptoquote: List<String>,
    val dxfeed: List<String>,
    val oanda: List<String>,
    val simulation: List<String>
)