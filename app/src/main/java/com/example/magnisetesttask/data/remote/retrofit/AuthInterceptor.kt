package com.example.magnisetesttask.data.remote.retrofit

import com.example.magnisetesttask.data.local.AccessTokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val accessTokenStorage: AccessTokenStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking { accessTokenStorage.getToken() }
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}