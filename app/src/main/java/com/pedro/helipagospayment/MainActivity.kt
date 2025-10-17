package com.pedro.helipagospayment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pedro.helipagospayment.common.ui.components.AppScaffold
import com.pedro.helipagospayment.navigation.AppNavigation
import com.pedro.helipagospayment.ui.theme.HelipagosPaymentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelipagosPaymentTheme {
                AppScaffold()
            }
        }
    }
}