package com.example.magnisetesttask.data.remote.retrofit.dto

import com.google.gson.annotations.SerializedName

data class DateRangeResponse(
    @SerializedName("data") val data: List<DateRangeData>
)

data class DateRangeData(
    @SerializedName("c") val close: Double,
    @SerializedName("h") val high: Double,
    @SerializedName("l") val low: Double,
    @SerializedName("o") val open: Double,
    @SerializedName("t") val timestamp: String,
    @SerializedName("v") val volume: Int
)