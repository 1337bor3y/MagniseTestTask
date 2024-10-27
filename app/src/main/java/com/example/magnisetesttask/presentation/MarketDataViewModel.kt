package com.example.magnisetesttask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magnisetesttask.domain.repository.MarketDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MarketDataViewModel @Inject constructor(
    private val marketDataRepository: MarketDataRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MarketDataState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    symbols = marketDataRepository.getSymbols()
                )
            }
            marketDataRepository.getRealTimeMarketData().collect { data ->
                _state.update {
                    it.copy(
                        time = formatTimestampToLocal(data.timestamp),
                        price = data.price.toString(),
                    )
                }
            }
        }
    }

    fun onEvent(event: MarketDataEvent) {
        when (event) {
            is MarketDataEvent.ChooseSymbol -> chooseSymbol(event.symbolId, event.symbolName)
        }
    }

    private fun chooseSymbol(symbolId: String, symbolName: String) {
        marketDataRepository.subscribeToMarketData(symbolId)
        _state.update { it.copy(selectedSymbol = symbolName) }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    historicalPrices = marketDataRepository.getHistoricalPrices(
                        instrumentId = symbolId,
                        startDate = "2023-08-07"
                    )
                )
            }
        }
    }

    private fun formatTimestampToLocal(timestamp: String): String {
        val zonedDateTime = ZonedDateTime.parse(timestamp)
        val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMM d, h:mm a")
        return localDateTime.format(formatter)
    }
}