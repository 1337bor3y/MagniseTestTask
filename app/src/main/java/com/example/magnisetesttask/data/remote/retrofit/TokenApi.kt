package com.example.magnisetesttask.data.remote.retrofit

import com.example.magnisetesttask.core.util.Constants
import com.example.magnisetesttask.data.remote.retrofit.dto.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface TokenApi {

    @FormUrlEncoded
    @POST("/identity/realms/{realm}/protocol/openid-connect/token")
    suspend fun getToken(
        @Path("realm") realm: String,
        @Field("grant_type") grantType: String = Constants.GRANT_TYPE,
        @Field("client_id") clientId: String = Constants.CLIENT_ID,
        @Field("username") username: String,
        @Field("password") password: String
    ): TokenResponse
}