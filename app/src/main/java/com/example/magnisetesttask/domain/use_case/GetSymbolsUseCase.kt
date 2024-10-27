package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.model.Symbol
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSymbolsUseCase @Inject constructor(
    private val marketDataRepository: MarketDataRepository
) {
    operator fun invoke(provider: String): Flow<Resource<List<Symbol>>> = flow {
        emit(Resource.Loading())
        val symbols = marketDataRepository.getSymbols(provider)
        emit(Resource.Success(symbols))
    }.catch { e ->
        emit(
            Resource.Error(
                e.localizedMessage
                    ?: "An unexpected error occurred when downloading the history"
            )
        )
    }
}