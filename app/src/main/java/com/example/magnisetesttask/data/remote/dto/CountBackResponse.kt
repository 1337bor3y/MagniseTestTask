package com.example.magnisetesttask.data.remote.dto

data class CountBackResponse(
    val data: List<CountBackData>
)

data class CountBackData(
    val c: Double,
    val h: Double,
    val l: Double,
    val o: Double,
    val t: String,
    val v: Int
)