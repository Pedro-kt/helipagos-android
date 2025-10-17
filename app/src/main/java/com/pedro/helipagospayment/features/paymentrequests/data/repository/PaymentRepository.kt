package com.pedro.helipagospayment.features.paymentrequests.data.repository

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentsResponseDto

interface PaymentRepository {
    suspend fun getPayments(): Result<PaymentsResponseDto>

    suspend fun getPaymentDetail(paymentId: Int): Result<PaymentResponseDto>
}