package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.model.HistoricalPrice
import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHistoricalPricesUseCase @Inject constructor(
    private val marketDataRepository: MarketDataRepository
) {
    operator fun invoke(
        symbolId: String,
        startDate: String,
        provider: String,
        interval: Int,
        periodicity: String
    ): Flow<Resource<List<HistoricalPrice>>> = flow {
        emit(Resource.Loading())
        val prices = marketDataRepository.getHistoricalPrices(
            instrumentId = symbolId,
            startDate = startDate,
            provider = provider,
            interval = interval,
            periodicity = periodicity
        )
        emit(Resource.Success(prices))
    }.catch { e ->
        emit(
            Resource.Error(
                e.localizedMessage
                    ?: "An unexpected error occurred when downloading the history"
            )
        )
    }
}