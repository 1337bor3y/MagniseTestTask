package com.example.magnisetesttask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magnisetesttask.domain.model.Resource
import com.example.magnisetesttask.domain.use_case.GetHistoricalPricesUseCase
import com.example.magnisetesttask.domain.use_case.GetRealTimeDataUseCase
import com.example.magnisetesttask.domain.use_case.GetSymbolsUseCase
import com.example.magnisetesttask.domain.use_case.SubscribeToMarketDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketDataViewModel @Inject constructor(
    private val getSymbolsUseCase: GetSymbolsUseCase,
    private val getRealTimeDataUseCase: GetRealTimeDataUseCase,
    private val subscribeToMarketDataUseCase: SubscribeToMarketDataUseCase,
    private val getHistoricalPricesUseCase: GetHistoricalPricesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MarketDataState())
    val state = _state.asStateFlow()

    init {
        setUpConnection()
    }

    fun onEvent(event: MarketDataEvent) {
        when (event) {
            is MarketDataEvent.ChooseSymbol -> chooseSymbol(event.symbolId, event.symbolName)
            MarketDataEvent.RetryConnection -> {
                setUpConnection()
                getHistoricalData(state.value.symbolId)
            }
        }
    }

    private fun setUpConnection() {
        viewModelScope.launch {
            val symbolsResult = getSymbolsUseCase("simulation").last()
            val dataResult = getRealTimeDataUseCase().last()
            if (symbolsResult.message != null || dataResult.message != null) {
                _state.update {
                    it.copy(
                        error = symbolsResult.message ?: dataResult.message
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        symbols = symbolsResult.data ?: emptyList(),
                        error = null
                    )
                }
                dataResult.data?.collect { data ->
                    _state.update {
                        it.copy(
                            time = data.timestamp,
                            price = data.price.toString(),
                        )
                    }
                }
            }
        }
    }

    private fun chooseSymbol(symbolId: String, symbolName: String) {
        subscribeToMarketDataUseCase(symbolId)
        _state.update { it.copy(selectedSymbol = symbolName, symbolId = symbolId) }
        getHistoricalData(symbolId)
    }

    private fun getHistoricalData(symbolId: String) {
        getHistoricalPricesUseCase(
            symbolId = symbolId,
            provider = "simulation",
            startDate = "2023-08-07",
            interval = 1,
            periodicity = "minute"
        ).onEach { result ->
            when (result) {
                is Resource.Error ->
                    _state.update {
                        it.copy(
                            error = result.message,
                            isGraphLoading = false
                        )
                    }

                is Resource.Loading -> _state.update {
                    it.copy(
                        error = null,
                        isGraphLoading = true
                    )
                }

                is Resource.Success ->
                    _state.update {
                        it.copy(
                            historicalPrices = result.data ?: emptyList(),
                            isGraphLoading = false
                        )
                    }
            }
        }.launchIn(viewModelScope)
    }
}