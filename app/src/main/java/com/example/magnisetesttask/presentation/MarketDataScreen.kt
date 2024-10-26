package com.example.magnisetesttask.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketDataScreen(
    viewModel: MarketDataViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent
    var expanded by remember { mutableStateOf(false) }
    val closePrices = state.historicalPrices.map { it.close }
    val maxPrice = closePrices.maxOrNull() ?: 0.0
    val minPrice = closePrices.minOrNull() ?: 0.0
    val priceRange = maxPrice - minPrice

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Spacer(modifier = Modifier.height(56.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = state.selectedSymbol,
                onValueChange = { },
                label = { Text("Select a symbol") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                state.symbols.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.symbol) },
                        onClick = {
                            expanded = false
                            onEvent(
                                MarketDataEvent.ChooseSymbol(item.id, item.symbol)
                            )
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Market data:",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Symbol\n" + state.selectedSymbol,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Price\n" + state.price,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Time\n" + state.time,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Charting data:",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 16.dp)
        ) {
            if (state.historicalPrices.isNotEmpty() && priceRange > 0) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    val spacePerPoint = size.width / (state.historicalPrices.size - 1)
                    val yScale = size.height / priceRange.toFloat()

                    drawLine(
                        color = Color.Black,
                        start = Offset(40f, 0f),
                        end = Offset(40f, size.height),
                        strokeWidth = 8f
                    )

                    for (i in 1..3) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(30f, i * size.height / 4),
                            end = Offset(40f, i * size.height / 4),
                            strokeWidth = 8f
                        )
                    }

                    drawLine(
                        color = Color.Black,
                        start = Offset(40f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 8f
                    )

                    var previousPoint = Offset(
                        40f,
                        size.height - (closePrices[0] - minPrice).toFloat() * yScale
                    )
                    for (i in 1 until closePrices.size) {
                        val currentX = 40f + i * spacePerPoint
                        val currentY =
                            size.height - (closePrices[i] - minPrice).toFloat() * yScale

                        val controlPoint = Offset(
                            (previousPoint.x + currentX) / 2,
                            previousPoint.y
                        )

                        drawLine(
                            color = Color.Black,
                            start = previousPoint,
                            end = controlPoint,
                            strokeWidth = 4f
                        )

                        drawLine(
                            color = Color.Black,
                            start = controlPoint,
                            end = Offset(currentX, currentY),
                            strokeWidth = 4f
                        )

                        previousPoint = Offset(currentX, currentY)
                    }
                }
            } else {
                Text(
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally),
                    text = "No data available for selected symbol",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
