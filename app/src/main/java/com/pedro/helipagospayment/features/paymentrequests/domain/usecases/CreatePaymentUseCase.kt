package com.pedro.helipagospayment.features.paymentrequests.domain.usecases

import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.repository.PaymentRepository
import javax.inject.Inject

class CreatePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(request: CreatePaymentRequestDto): Result<CreatePaymentResponseDto> {
        return paymentRepository.createPayment(request)
    }
}