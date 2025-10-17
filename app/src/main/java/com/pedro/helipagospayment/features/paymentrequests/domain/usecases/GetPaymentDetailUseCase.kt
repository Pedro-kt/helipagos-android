package com.pedro.helipagospayment.features.paymentrequests.domain.usecases

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentDetailUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: Int): Result<PaymentResponseDto> {
        return paymentRepository.getPaymentDetail(paymentId)
    }
}