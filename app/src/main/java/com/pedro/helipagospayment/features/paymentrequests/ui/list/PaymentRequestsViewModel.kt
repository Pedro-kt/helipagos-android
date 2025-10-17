package com.pedro.helipagospayment.features.paymentrequests.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.GetPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentRequestsViewModel @Inject constructor(
    private val getPaymentUseCase: GetPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PaymentRequestsUiState>(PaymentRequestsUiState.Loading)
    val uiState: StateFlow<PaymentRequestsUiState> = _uiState.asStateFlow() // ahora esto se expone como ReadOnly


    init {
        loadPayments()
    }

    // Cambie esta funcion de privada a publica para lograr el Retry desde la UI
    fun loadPayments() {
        viewModelScope.launch {
            _uiState.value = PaymentRequestsUiState.Loading

            try {
                val payments = getPaymentUseCase()
                _uiState.value = PaymentRequestsUiState.Success(payments)
            } catch (e: Exception) {
                _uiState.value = PaymentRequestsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun retry() {
        loadPayments()
    }
}