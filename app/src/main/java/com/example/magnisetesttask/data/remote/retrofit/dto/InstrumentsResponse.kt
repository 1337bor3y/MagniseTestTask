package com.example.magnisetesttask.data.remote.retrofit.dto

data class InstrumentsResponse(
    val data: List<InstrumentsData>,
    val paging: Paging
)

data class InstrumentsData(
    val baseCurrency: String,
    val currency: String,
    val description: String,
    val id: String,
    val kind: String,
    val mappings: Mappings,
    val profile: Profile,
    val symbol: String,
    val tickSize: Double
)

data class Profile(
    val gics: List<String>,
    val name: String
)

data class Paging(
    val items: Int,
    val page: Int,
    val pages: Int
)

data class Mappings(
    val activeTick: ActiveTick,
    val dxfeed: Dxfeed,
    val oanda: Oanda,
    val simulation: Simulation
)

data class ActiveTick(
    val defaultOrderSize: Int,
    val exchange: String,
    val symbol: String
)

data class Dxfeed(
    val defaultOrderSize: Int,
    val exchange: String,
    val symbol: String
)

data class Oanda(
    val defaultOrderSize: Int,
    val exchange: String,
    val symbol: String
)

data class Simulation(
    val defaultOrderSize: Int,
    val exchange: String,
    val symbol: String
)