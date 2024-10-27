package com.example.magnisetesttask.domain.use_case

import com.example.magnisetesttask.domain.model.MarketData
import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetRealTimeDataUseCase @Inject constructor(
    private val marketDataRepository: MarketDataRepository
) {
    operator fun invoke(): Flow<Resource<Flow<MarketData>>> = flow {
        emit(Resource.Loading())
        val flowData = marketDataRepository.getRealTimeMarketData()
            .map { data ->
                data.copy(
                    timestamp = formatTimestampToLocal(data.timestamp)
                )
            }
        emit(Resource.Success(flowData))
    }.catch { e ->
        emit(
            Resource.Error(
                e.localizedMessage
                    ?: "An unexpected error occurred when downloading the history"
            )
        )
    }

    private fun formatTimestampToLocal(timestamp: String): String {
        val zonedDateTime = ZonedDateTime.parse(timestamp)
        val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMM d, h:mm a")
        return localDateTime.format(formatter)
    }
}

