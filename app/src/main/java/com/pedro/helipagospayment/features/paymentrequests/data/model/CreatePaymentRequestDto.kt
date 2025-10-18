package com.pedro.helipagospayment.features.paymentrequests.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentRequestDto(
    val importe: Int,
    @SerialName("fecha_vto") val fechaVencimiento: String,
    val descripcion: String,
    @SerialName("referencia_externa") val referenciaExterna: String,
    @SerialName("url_redirect") val urlRedirect: String,
    val webhook: String
)
