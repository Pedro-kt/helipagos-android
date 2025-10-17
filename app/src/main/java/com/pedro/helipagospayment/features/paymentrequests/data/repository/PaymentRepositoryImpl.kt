package com.pedro.helipagospayment.features.paymentrequests.data.repository

import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentsResponseDto
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun getPayments(): Result<PaymentsResponseDto> {
        return try {
            val response = api.getPayments()
            Result.success(PaymentsResponseDto(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPaymentDetail(paymentId: Int): Result<PaymentResponseDto> {
        return try {
            val response = api.getPaymentDetail(paymentId)
            if (response.isNotEmpty()) {
                Result.success(response.first())
            } else {
                Result.failure(Exception("Payment not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}