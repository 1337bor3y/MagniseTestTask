package com.example.magnisetesttask.core.di

import com.example.magnisetesttask.core.util.Constants
import com.example.magnisetesttask.data.remote.FintachartsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNumbersApi(): FintachartsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_REST_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FintachartsApi::class.java)
    }
}