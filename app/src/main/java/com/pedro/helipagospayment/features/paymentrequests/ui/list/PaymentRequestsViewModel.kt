package com.pedro.helipagospayment.features.paymentrequests.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.GetPaymentsPagedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentRequestsViewModel @Inject constructor(
    getPaymentsPagedUseCase: GetPaymentsPagedUseCase
) : ViewModel() {

    val paymentsPagingData: Flow<PagingData<PaymentResponseDto>> =
        getPaymentsPagedUseCase()
            .cachedIn(viewModelScope)
}