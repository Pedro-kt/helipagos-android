package com.pedro.helipagospayment.features.paymentrequests.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.CreatePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePaymentViewModel @Inject constructor(
    private val createPaymentUseCase: CreatePaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreatePaymentUiState>(CreatePaymentUiState.Idle)
    val uiState: StateFlow<CreatePaymentUiState> = _uiState

    fun createPayment(request: CreatePaymentRequestDto) {
        viewModelScope.launch {
            _uiState.value = CreatePaymentUiState.Loading
            val result = createPaymentUseCase(request)
            _uiState.value = result.fold(
                onSuccess = { CreatePaymentUiState.Success(it) },
                onFailure = { CreatePaymentUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

}