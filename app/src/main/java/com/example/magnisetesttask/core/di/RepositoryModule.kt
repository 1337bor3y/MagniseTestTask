package com.example.magnisetesttask.core.di

import com.example.magnisetesttask.data.repository.MarketDataRepositoryImpl
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMarketDataRepository(
        marketDataRepositoryImpl: MarketDataRepositoryImpl
    ): MarketDataRepository
}