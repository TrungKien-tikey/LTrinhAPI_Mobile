package com.example.mobile_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.mobile_api.screens.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Thiết lập theme cơ bản
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Gọi file Navigation
                    AppNavigation()
                }
            }
        }
    }
}