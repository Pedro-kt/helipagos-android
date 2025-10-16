package com.pedro.helipagospayment.features.paymentrequest.domain.usecases

import com.pedro.helipagospayment.features.paymentrequest.data.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke() = repository.getPayments()
}