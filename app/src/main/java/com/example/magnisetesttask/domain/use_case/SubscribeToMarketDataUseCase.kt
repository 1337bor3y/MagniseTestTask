package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.repository.MarketDataRepository
import javax.inject.Inject

class SubscribeToMarketDataUseCase @Inject constructor(
    private val marketDataRepository: MarketDataRepository
) {
    operator fun invoke(symbolId: String) = marketDataRepository.subscribeToMarketData(symbolId)
}