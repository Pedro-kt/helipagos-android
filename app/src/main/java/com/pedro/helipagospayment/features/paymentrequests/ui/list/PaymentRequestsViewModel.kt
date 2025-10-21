package com.pedro.helipagospayment.features.paymentrequests.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentPagedDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.GetPaymentsPagedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PaymentRequestsViewModel @Inject constructor(
    getPaymentsPagedUseCase: GetPaymentsPagedUseCase
) : ViewModel() {

    val paymentsPagingData: Flow<PagingData<PaymentPaged>> =
        getPaymentsPagedUseCase()
            .cachedIn(viewModelScope)
}