package com.pedro.helipagospayment.features.paymentrequests.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PaymentDetailScreen(
    paymentId: Int
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Payment ID: $paymentId")
    }
}