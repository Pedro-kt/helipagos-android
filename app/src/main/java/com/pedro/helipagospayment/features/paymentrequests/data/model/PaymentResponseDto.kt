package com.pedro.helipagospayment.features.paymentrequests.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponseDto(
    @SerialName("id_sp") val idSp: Int,
    @SerialName("estado_pago") val estadoPago: String? = null,
    @SerialName("fecha_pago") val fechaPago: String? = null,
    @SerialName("medio_pago") val medioPago: String? = null,
    @SerialName("importe_pagado") val importePagado: Double? = null,
    val descripcion: String = "",
    val importe: Double = 0.0,
    @SerialName("referencia_externa") val referenciaExterna: String = "",
    @SerialName("codigo_barra") val codigoBarra: String? = null,
    @SerialName("fecha_acreditacion") val fechaAcreditacion: String? = null,
    @SerialName("referencia_externa_2") val referenciaExterna2: String? = null,
    @SerialName("fecha_vencimiento") val fechaVencimiento: String = "",
    @SerialName("importe_vencido") val importeVencido: Double? = null,
    @SerialName("fecha_creacion") val fechaCreacion: String = "",
    val cuotas: Int? = null,
    @SerialName("fecha_actualizacion") val fechaActualizacion: String = "",
    @SerialName("segunda_fecha_vencimiento") val segundaFechaVencimiento: String? = null,
    @SerialName("checkout_url") val checkoutUrl: String? = null,
    @SerialName("email_pagador") val emailPagador: String? = null
)
