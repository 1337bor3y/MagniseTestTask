package com.example.magnisetesttask.data.local

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.magnisetesttask.core.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = Constants.PREFERENCE_NAME)

class DataStoreAccessTokenStorage @Inject constructor(
    private val application: Application
) : AccessTokenStorage {

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val EXPIRATION_KEY = longPreferencesKey("expires_in")
    }

    override suspend fun saveToken(accessToken: String, expiresIn: Int) {
        application.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[EXPIRATION_KEY] = (System.currentTimeMillis() + expiresIn * 1000)
        }
    }

    override suspend fun getToken(): String? {
        return application.dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    override suspend fun isTokenExpired(): Boolean {
        val expirationTime = application.dataStore.data.map { preferences ->
            preferences[EXPIRATION_KEY] ?: 0
        }.first()
        return System.currentTimeMillis() >= expirationTime
    }

    override suspend fun clearToken() {
        application.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}