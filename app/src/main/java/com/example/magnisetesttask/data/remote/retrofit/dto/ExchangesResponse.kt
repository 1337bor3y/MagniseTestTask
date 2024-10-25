package com.example.magnisetesttask.data.remote.retrofit.dto

import com.google.gson.annotations.SerializedName

data class ExchangesResponse(
    @SerializedName("data") val data: ExchangesData
)

data class ExchangesData(
    @SerializedName("activeTick") val activeTick: List<String>,
    @SerializedName("alpaca") val alpaca: List<String>,
    @SerializedName("cryptoquote") val cryptoquote: List<String>,
    @SerializedName("dxfeed") val dxfeed: List<String>,
    @SerializedName("oanda") val oanda: List<String>,
    @SerializedName("simulation") val simulation: List<String>
)