package com.example.magnisetesttask.data.remote.retrofit.dto

data class DateRangeResponse(
    val data: List<DateRangeData>
)

data class DateRangeData(
    val c: Double,
    val h: Double,
    val l: Double,
    val o: Double,
    val t: String,
    val v: Int
)