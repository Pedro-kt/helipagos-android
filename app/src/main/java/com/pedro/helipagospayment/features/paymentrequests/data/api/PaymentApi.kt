package com.pedro.helipagospayment.features.paymentrequests.data.api

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface PaymentApi {

    @POST("api/solicitud_pago/v1/get_solicitud_pago")
    suspend fun getPayments(): List<PaymentResponseDto>

    @POST("api/solicitud_pago/v1/get_solicitud_pago/{id}")
    suspend fun getPaymentDetail(
        @Query("id") paymentId: Int
    ): List<PaymentResponseDto>

}