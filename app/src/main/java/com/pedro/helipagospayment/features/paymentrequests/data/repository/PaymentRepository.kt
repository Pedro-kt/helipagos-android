package com.pedro.helipagospayment.features.paymentrequest.data.repository

import com.pedro.helipagospayment.features.paymentrequest.data.model.PaymentsResponseDto

interface PaymentRepository {
    suspend fun getPayments(): Result<PaymentsResponseDto>
}