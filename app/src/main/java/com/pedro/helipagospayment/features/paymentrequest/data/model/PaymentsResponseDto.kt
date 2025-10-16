package com.pedro.helipagospayment.features.paymentrequest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentsResponseDto(
    val payments: List<PaymentResponseDto>
)
