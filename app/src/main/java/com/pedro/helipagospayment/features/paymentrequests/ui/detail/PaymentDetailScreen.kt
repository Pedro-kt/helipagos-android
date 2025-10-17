package com.pedro.helipagospayment.features.paymentrequests.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.pedro.helipagospayment.common.ui.components.ErrorView
import com.pedro.helipagospayment.common.ui.components.Loader
import com.pedro.helipagospayment.common.ui.components.PaymentDetailContent

@Composable
fun PaymentDetailScreen(
    viewModel: PaymentDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is PaymentDetailUiState.Loading -> Loader()
        is PaymentDetailUiState.Error -> ErrorView()
        is PaymentDetailUiState.Success -> {
            PaymentDetailContent((state as PaymentDetailUiState.Success).payment)
        }
    }
}