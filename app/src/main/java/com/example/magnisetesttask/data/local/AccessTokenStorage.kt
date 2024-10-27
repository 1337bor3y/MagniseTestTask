package com.example.magnisetesttask.data.local

interface AccessTokenStorage {

    suspend fun getToken(): String?
}