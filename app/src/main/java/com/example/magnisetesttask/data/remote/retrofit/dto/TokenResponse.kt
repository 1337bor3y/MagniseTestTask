package com.example.magnisetesttask.data.remote.retrofit.dto

data class TokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val notBeforePolicy: Int,
    val refreshExpiresIn: Int,
    val refreshToken: String,
    val scope: String,
    val sessionState: String,
    val tokenType: String
)