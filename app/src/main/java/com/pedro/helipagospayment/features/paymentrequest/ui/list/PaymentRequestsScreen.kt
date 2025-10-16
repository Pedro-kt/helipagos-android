package com.pedro.helipagospayment.features.paymentrequest.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pedro.helipagospayment.features.paymentrequest.data.model.PaymentResponseDto

@Composable
fun PaymentRequestsScreen(
    viewModel: PaymentRequestsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is PaymentRequestsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PaymentRequestsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Error: ${(state as PaymentRequestsUiState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is PaymentRequestsUiState.Success -> {
            val paymentsResult = (state as PaymentRequestsUiState.Success).payments
            paymentsResult.fold(
                onSuccess = { responseDto ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(responseDto.payments) { payment ->
                            PaymentItemCard(payment)
                        }
                    }
                },
                        onFailure = { throwable ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${throwable.localizedMessage ?: "Unknown error"}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PaymentItemCard(payment: PaymentResponseDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = payment.descripcion,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estado: ${payment.estado_pago ?: "Desconocido"}")
            Text(text = "Importe: $${payment.importe}")
            Text(text = "Medio: ${payment.medio_pago ?: "N/A"}")

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${payment.fecha_pago ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
