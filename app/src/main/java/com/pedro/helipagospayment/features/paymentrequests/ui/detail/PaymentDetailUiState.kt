package com.pedro.helipagospayment.features.paymentrequests.ui.detail

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentResponse

sealed class PaymentDetailUiState {
    object Loading : PaymentDetailUiState()
    data class Success(val payment: PaymentResponse) : PaymentDetailUiState()
    data class Error(val message: String) : PaymentDetailUiState()
}