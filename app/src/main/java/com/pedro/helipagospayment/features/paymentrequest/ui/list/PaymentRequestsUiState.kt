package com.pedro.helipagospayment.features.paymentrequest.ui.list

import com.pedro.helipagospayment.features.paymentrequest.data.model.PaymentsResponseDto

sealed class PaymentRequestsUiState {
    object Loading : PaymentRequestsUiState()
    data class Success(val payments: Result<PaymentsResponseDto>) : PaymentRequestsUiState()
    data class Error(val message: String) : PaymentRequestsUiState()
}