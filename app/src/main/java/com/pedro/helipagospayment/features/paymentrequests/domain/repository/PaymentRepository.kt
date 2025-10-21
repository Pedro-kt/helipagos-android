package com.pedro.helipagospayment.features.paymentrequests.domain.repository

import androidx.paging.PagingData
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {

    suspend fun getPaymentDetail(paymentId: Int): Result<PaymentResponseDto>

    suspend fun createPayment(request: CreatePaymentRequestDto): Result<CreatePaymentResponseDto>

    fun getPaymentsPaged(): Flow<PagingData<PaymentPaged>>
}