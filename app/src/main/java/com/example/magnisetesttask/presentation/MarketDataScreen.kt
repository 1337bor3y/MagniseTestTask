package com.example.magnisetesttask.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = state.selectedSymbol,
                onValueChange = { },
                label = { Text("Select an option") },
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
            text = "Market data:",
            style = MaterialTheme
                .typography.bodySmall
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Symbol\n" + state.selectedSymbol,
                style = MaterialTheme
                    .typography.bodySmall
            )
            Text(
                text = "Price\n" + state.price,
                style = MaterialTheme
                    .typography.bodySmall
            )
            Text(
                text = "Time\n" + state.time,
                style = MaterialTheme
                    .typography.bodySmall
            )
        }
    }
}