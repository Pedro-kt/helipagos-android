package com.pedro.helipagospayment.features.paymentrequests.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.GetPaymentDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentDetailViewModel @Inject constructor(
    private val getPaymentDetailUseCase: GetPaymentDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val paymentId: Int = checkNotNull(savedStateHandle["paymentId"]) {
        "paymentId is required"
    }

    private val _uiState = MutableStateFlow<PaymentDetailUiState>(PaymentDetailUiState.Loading)
    val uiState: StateFlow<PaymentDetailUiState> = _uiState.asStateFlow()

    init {
        loadPaymentDetail()
    }

    private fun loadPaymentDetail() {
        viewModelScope.launch {
            _uiState.value = PaymentDetailUiState.Loading

            getPaymentDetailUseCase(paymentId).fold(
                onSuccess = { payment ->
                    _uiState.value = PaymentDetailUiState.Success(payment)
                },
                onFailure = { error ->
                    _uiState.value = PaymentDetailUiState.Error(
                        error.message ?: "Error al cargar el detalle"
                    )
                }
            )
        }
    }

    //metodo retry para futura implementacion de recarga
    fun retry() {
        loadPaymentDetail()
    }
}