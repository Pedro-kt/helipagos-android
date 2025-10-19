package com.pedro.helipagospayment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pedro.helipagospayment.common.ui.components.AppScaffold
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