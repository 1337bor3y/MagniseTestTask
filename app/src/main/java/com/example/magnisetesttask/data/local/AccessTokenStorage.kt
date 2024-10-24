package com.example.magnisetesttask.data.local

interface AccessTokenStorage {

    suspend fun saveToken(accessToken: String, expiresIn: Int)

    suspend fun getToken(): String?

    suspend fun isTokenExpired(): Boolean

    suspend fun clearToken()
}