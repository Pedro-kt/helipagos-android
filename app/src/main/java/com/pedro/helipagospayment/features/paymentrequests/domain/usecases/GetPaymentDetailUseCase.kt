package com.pedro.helipagospayment.features.paymentrequests.domain.usecases

import com.pedro.helipagospayment.features.paymentrequests.data.mapper.toPaymentResponse
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentResponse
import com.pedro.helipagospayment.features.paymentrequests.domain.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentDetailUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: Int): Result<PaymentResponse> {
        return paymentRepository.getPaymentDetail(paymentId).map { it.toPaymentResponse() }
    }
}