package com.example.magnisetesttask.data.remote.retrofit

import android.util.Log
import com.example.magnisetesttask.data.local.AccessTokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val accessTokenStorage: AccessTokenStorage,
    private val tokenApi: TokenApi
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking {
            if (accessTokenStorage.isTokenExpired()) {
                try {
                    val response = tokenApi.getToken(
                        realm = "fintatech",
                        username = "r_test@fintatech.com",
                        password = "kisfiz-vUnvy9-sopnyv"
                    )
                    accessTokenStorage.saveToken(response.accessToken, response.expiresIn)
                    response.accessToken
                } catch (e: Exception) {
                    Log.d("AuthInterceptor", "Token retrieval error: ${e.localizedMessage}")
                    null
                }
            } else {
                accessTokenStorage.getToken()
            }
        }

        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}