package com.pedro.helipagospayment.features.paymentrequests.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentResponseDto(
    @SerialName("id_sp") val idSp: Int,
    @SerialName("id_cliente") val idCliente: Int,
    @SerialName("estado") val estado: String,
    @SerialName("referencia_externa") val referenciaExterna: String,
    @SerialName("fecha_creacion") val fechaCreacion: String,
    @SerialName("descripcion") val descripcion: String,
    @SerialName("codigo_barra") val codigoBarra: String,
    @SerialName("id_url") val idUrl: String,
    @SerialName("checkout_url") val checkoutUrl: String,
    @SerialName("short_url") val shortUrl: String,
    @SerialName("fecha_vencimiento") val fechaVencimiento: String,
    @SerialName("importe") val importe: Int
)