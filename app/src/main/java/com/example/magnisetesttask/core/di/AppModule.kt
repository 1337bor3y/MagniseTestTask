package com.example.magnisetesttask.core.di

import android.app.Application
import com.example.magnisetesttask.core.util.Constants
import com.example.magnisetesttask.data.local.AccessTokenStorage
import com.example.magnisetesttask.data.local.DataStoreAccessTokenStorage
import com.example.magnisetesttask.data.remote.retrofit.AuthInterceptor
import com.example.magnisetesttask.data.remote.retrofit.FintachartsApi
import com.example.magnisetesttask.data.remote.retrofit.TokenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenApi(): TokenApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_REST_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFintachartsApi(
        client: OkHttpClient
    ): FintachartsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_REST_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FintachartsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        accessTokenStorage: AccessTokenStorage,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(accessTokenStorage))
            .build()
    }

    @Provides
    @Singleton
    fun provideAccessTokenStorage(
        application: Application,
        tokenApi: TokenApi
    ): AccessTokenStorage {
        return DataStoreAccessTokenStorage(application, tokenApi)
    }
}