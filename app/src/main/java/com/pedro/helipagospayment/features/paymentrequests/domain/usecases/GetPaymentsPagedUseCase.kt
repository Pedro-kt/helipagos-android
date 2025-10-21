package com.pedro.helipagospayment.features.paymentrequests.domain.usecases

import androidx.paging.PagingData
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged
import com.pedro.helipagospayment.features.paymentrequests.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentsPagedUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    operator fun invoke(): Flow<PagingData<PaymentPaged>> {
        return repository.getPaymentsPaged()
    }
}