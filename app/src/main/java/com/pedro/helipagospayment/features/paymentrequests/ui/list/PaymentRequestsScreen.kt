package com.pedro.helipagospayment.features.paymentrequests.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pedro.helipagospayment.common.ui.components.ErrorView
import com.pedro.helipagospayment.common.ui.components.Loader
import com.pedro.helipagospayment.common.ui.components.PaymentItemCard

@Composable
fun PaymentRequestsScreen(
    viewModel: PaymentRequestsViewModel = hiltViewModel(),
    onPaymentClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is PaymentRequestsUiState.Loading -> {
            Loader()
        }

        is PaymentRequestsUiState.Error -> {
            ErrorView()
        }

        is PaymentRequestsUiState.Success -> {

            val paymentsResult = (state as PaymentRequestsUiState.Success).payments

            paymentsResult.fold(
                onSuccess = { responseDto ->
                    if (responseDto.payments.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No hay solicitudes de pago disponibles.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(responseDto.payments) { payment ->
                                PaymentItemCard(
                                    payment,
                                    onClick = {
                                        onPaymentClick(payment.idSp)
                                    }
                                )
                            }
                        }
                    }
                },
                onFailure = { throwable ->
                    ErrorView()
                }
            )
        }
    }
}