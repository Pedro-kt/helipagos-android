package com.pedro.helipagospayment.features.paymentrequests.ui.create

import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentResponseDto

sealed class CreatePaymentUiState {
    object Idle : CreatePaymentUiState()
    object Loading : CreatePaymentUiState()
    data class Success(val data: CreatePaymentResponseDto) : CreatePaymentUiState()
    data class Error(val message: String) : CreatePaymentUiState()
}