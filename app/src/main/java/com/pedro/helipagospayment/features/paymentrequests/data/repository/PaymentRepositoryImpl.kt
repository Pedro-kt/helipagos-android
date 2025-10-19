package com.pedro.helipagospayment.features.paymentrequests.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentsResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.paging.PaymentPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    private var pagingSource: PaymentPagingSource? = null

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
            // Invalidar cache cuando se crea un pago nuevo
            pagingSource?.clearCache()
            pagingSource?.invalidate()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPaymentsPaged(): Flow<PagingData<PaymentResponseDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10,
                prefetchDistance = 3
            ),
            pagingSourceFactory = {
                PaymentPagingSource(api).also { pagingSource = it }
            }
        ).flow
    }

}