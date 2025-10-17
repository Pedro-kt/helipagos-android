package com.pedro.helipagospayment.features.paymentrequests.data.repository

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentsResponseDto

interface PaymentRepository {
    suspend fun getPayments(): Result<PaymentsResponseDto>
}