package com.example.magnisetesttask.data.remote

import com.example.magnisetesttask.data.remote.dto.ExchangesResponse
import com.example.magnisetesttask.data.remote.dto.InstrumentsResponse
import com.example.magnisetesttask.data.remote.dto.ProvidersResponse
import com.example.magnisetesttask.data.remote.dto.TokenResponse
import com.example.magnisetesttask.data.remote.dto.TokenRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FintachartsApi {

    @POST("/identity/realms/{realm}/protocol/openid-connect/token")
    suspend fun getToken(
        @Path("realm") realm: String,
        @Body request: TokenRequest
    ): TokenResponse

    @GET("api/instruments/v1/instruments")
    suspend fun getInstruments(
        @Query("provider") provider: String,
        @Query("kind") kind: String
    ): InstrumentsResponse

    @GET("/api/instruments/v1/providers")
    suspend fun getProviders(): ProvidersResponse

    @GET("/api/instruments/v1/exchanges")
    suspend fun getExchanges(): ExchangesResponse
}