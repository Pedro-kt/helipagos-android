package com.pedro.helipagospayment.features.paymentrequests.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentPagedDto(
    @SerialName("id_sp") val idSp: Int,
    @SerialName("codigo_barra") val codigoBarra: String? = null,
    @SerialName("estado_pago") val estadoPago: String? = null,
    @SerialName("medio_pago") val medioPago: String? = null,
    @SerialName("descripcion") val descripcion: String? = null,
    @SerialName("importe_pagado") val importePagado: String? = null,
    @SerialName("referencia_externa") val referenciaExterna: String? = null,
    @SerialName("referencia_externa_2") val referenciaExternaTwo: String? = null,
    @SerialName("fecha_creacion") val fechaCreacion: String? = null,
    @SerialName("fecha_pago") val fechaPago: String? = null,
    @SerialName("fecha_contracargo") val fechaContracargo: String? = null,
    @SerialName("fecha_vencimiento") val fechaVencimiento: String? = null,
    @SerialName("segunda_fecha_vencimiento") val segundaFechaVencimiento: String? = null,
)
