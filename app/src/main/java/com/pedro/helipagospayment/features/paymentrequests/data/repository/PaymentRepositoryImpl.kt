package com.pedro.helipagospayment.features.paymentrequests.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import kotlinx.coroutines.flow.Flow
import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.paging.PaymentPagingSource
import com.pedro.helipagospayment.features.paymentrequests.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    override fun getPaymentsPaged(): Flow<PagingData<PaymentResponseDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { PaymentPagingSource(api) }
        ).flow
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

    override suspend fun createPayment(request: CreatePaymentRequestDto): Result<CreatePaymentResponseDto> {
        return try {
            val response = api.createPayment(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}