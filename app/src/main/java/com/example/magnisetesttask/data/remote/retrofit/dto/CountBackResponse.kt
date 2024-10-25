package com.example.magnisetesttask.data.remote.retrofit.dto

import com.google.gson.annotations.SerializedName

data class CountBackResponse(
    @SerializedName("data") val data: List<CountBackData>
)

data class CountBackData(
    @SerializedName("c") val close: Double,
    @SerializedName("h") val high: Double,
    @SerializedName("l") val low: Double,
    @SerializedName("o") val open: Double,
    @SerializedName("t") val timestamp: String,
    @SerializedName("v") val volume: Int
)