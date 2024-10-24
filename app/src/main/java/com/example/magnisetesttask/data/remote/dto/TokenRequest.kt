package com.example.magnisetesttask.data.remote.dto

import com.example.magnisetesttask.core.util.Constants

data class TokenRequest(
    val grantType: String = Constants.GRANT_TYPE,
    val clientId: String = Constants.CLIENT_ID,
    val username: String,
    val password: String
)