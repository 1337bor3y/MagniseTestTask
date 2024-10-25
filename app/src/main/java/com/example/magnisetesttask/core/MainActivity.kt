package com.example.magnisetesttask.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.magnisetesttask.core.theme.MagniseTestTaskTheme
import com.example.magnisetesttask.presentation.MarketDataScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagniseTestTaskTheme {
                MarketDataScreen()
            }
        }
    }
}