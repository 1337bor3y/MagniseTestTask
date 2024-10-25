package com.example.magnisetesttask.data.remote.retrofit

import com.example.magnisetesttask.data.remote.retrofit.dto.CountBackResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.DateRangeResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.ExchangesResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.InstrumentsResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.ProvidersResponse
import com.example.magnisetesttask.data.remote.retrofit.dto.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FintachartsApi {

    @FormUrlEncoded
    @POST("/identity/realms/{realm}/protocol/openid-connect/token")
    suspend fun getToken(
        @Path("realm") realm: String,
        @Field("grant_type") grantType: String = "password",
        @Field("client_id") clientId: String = "app-cli",
        @Field("username") username: String,
        @Field("password") password: String
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

    @GET("/api/bars/v1/bars/count-back")
    suspend fun getCountBack(
        @Query("instrumentId") instrumentId: String,
        @Query("provider") provider: String,
        @Query("interval") interval: Int,
        @Query("periodicity") periodicity: String,
        @Query("barsCount") barsCount: Int
    ): CountBackResponse

    @GET("/api/bars/v1/bars/date-range")
    suspend fun getDateRange(
        @Query("instrumentId") instrumentId: String,
        @Query("provider") provider: String,
        @Query("interval") interval: Int,
        @Query("periodicity") periodicity: String,
        @Query("startDate") startDate: String
    ): DateRangeResponse
}