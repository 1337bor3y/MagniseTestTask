package com.example.magnisetesttask.data.remote.retrofit.dto

import com.google.gson.annotations.SerializedName

data class ProvidersResponse(
    @SerializedName("data") val dataList: List<String>
)