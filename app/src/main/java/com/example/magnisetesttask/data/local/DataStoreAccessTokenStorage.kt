package com.example.magnisetesttask.data.local

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.magnisetesttask.core.util.Constants
import com.example.magnisetesttask.data.remote.retrofit.TokenApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = Constants.PREFERENCE_NAME)

class DataStoreAccessTokenStorage @Inject constructor(
    private val application: Application,
    private val tokenApi: TokenApi
) : AccessTokenStorage {

    private val mutex = Mutex()

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val EXPIRATION_KEY = longPreferencesKey("expires_in")
    }

    override suspend fun getToken(): String? {
        return mutex.withLock {
            var validToken = application.dataStore.data.first()[ACCESS_TOKEN_KEY]
            if (isTokenExpired()) {
                try {
                    val response = tokenApi.getToken(
                        realm = "fintatech",
                        username = "r_test@fintatech.com",
                        password = "kisfiz-vUnvy9-sopnyv"
                    )
                    validToken = response.accessToken
                    saveToken(validToken, response.expiresIn)
                } catch (e: Exception) {
                    Log.d(
                        "DataStoreAccessTokenStorage",
                        "Token retrieval error: ${e.localizedMessage}"
                    )
                }
            }
            validToken
        }
    }

    private suspend fun saveToken(accessToken: String, expiresIn: Int) {
        application.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[EXPIRATION_KEY] = (System.currentTimeMillis() + expiresIn * 1000)
        }
    }

    private suspend fun isTokenExpired(): Boolean {
        val expirationTime = application.dataStore.data.map { preferences ->
            preferences[EXPIRATION_KEY] ?: 0
        }.first()
        return System.currentTimeMillis() >= expirationTime
    }
}