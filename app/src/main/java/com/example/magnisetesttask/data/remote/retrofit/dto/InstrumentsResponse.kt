package com.example.magnisetesttask.data.remote.retrofit.dto

import com.google.gson.annotations.SerializedName


data class InstrumentsResponse(
    @SerializedName("data") val data: List<InstrumentsData>,
    @SerializedName("paging") val paging: Paging
)

data class InstrumentsData(
    @SerializedName("base_currency") val baseCurrency: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("mappings") val mappings: Mappings,
    @SerializedName("profile") val profile: Profile,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("tick_size") val tickSize: Double
)

data class Profile(
    @SerializedName("gics") val gics: List<String>,
    @SerializedName("name") val name: String
)

data class Paging(
    @SerializedName("items") val items: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int
)

data class Mappings(
    @SerializedName("active_tick") val activeTick: ActiveTick,
    @SerializedName("dxfeed") val dxfeed: Dxfeed,
    @SerializedName("oanda") val oanda: Oanda,
    @SerializedName("simulation") val simulation: Simulation
)

data class ActiveTick(
    @SerializedName("default_order_size") val defaultOrderSize: Int,
    @SerializedName("exchange") val exchange: String,
    @SerializedName("symbol") val symbol: String
)

data class Dxfeed(
    @SerializedName("default_order_size") val defaultOrderSize: Int,
    @SerializedName("exchange") val exchange: String,
    @SerializedName("symbol") val symbol: String
)

data class Oanda(
    @SerializedName("default_order_size") val defaultOrderSize: Int,
    @SerializedName("exchange") val exchange: String,
    @SerializedName("symbol") val symbol: String
)

data class Simulation(
    @SerializedName("default_order_size") val defaultOrderSize: Int,
    @SerializedName("exchange") val exchange: String,
    @SerializedName("symbol") val symbol: String
)