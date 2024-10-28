package com.example.magnisetesttask.data.remote.retrofit

import com.example.magnisetesttask.data.remote.retrofit.dto.DateRangeResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.InstrumentsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FintachartsApi {

    @GET("api/instruments/v1/instruments")
    suspend fun getInstruments(
        @Query("provider") provider: String
    ): InstrumentsResponse

    @GET("/api/bars/v1/bars/date-range")
    suspend fun getDateRange(
        @Query("instrumentId") instrumentId: String,
        @Query("provider") provider: String,
        @Query("interval") interval: Int,
        @Query("periodicity") periodicity: String,
        @Query("startDate") startDate: String
    ): DateRangeResponse
}