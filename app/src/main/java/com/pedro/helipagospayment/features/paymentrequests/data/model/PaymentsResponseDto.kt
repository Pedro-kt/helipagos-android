package com.pedro.helipagospayment.features.paymentrequests.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentsResponseDto(
    val payments: List<PaymentResponseDto>
)
