package com.pedro.helipagospayment.features.paymentrequests.ui.detail

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto

sealed class PaymentDetailUiState {
    object Loading : PaymentDetailUiState()
    data class Success(val payment: PaymentResponseDto) : PaymentDetailUiState()
    data class Error(val message: String) : PaymentDetailUiState()
}