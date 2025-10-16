package com.pedro.helipagospayment.features.paymentrequest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponseDto(
    val id_sp: Int,
    val estado_pago: String? = null,
    val fecha_pago: String? = null,
    val medio_pago: String? = null,
    val importe_pagado: Double? = null,
    val descripcion: String = "",
    val importe: Double = 0.0,
    val referencia_externa: String = "",
    val codigo_barra: String? = null,
    val fecha_acreditacion: String? = null,
    val referencia_externa_2: String? = null,
    val fecha_vencimiento: String = "",
    val importe_vencido: Double? = null,
    val fecha_creacion: String = "",
    val cuotas: Int? = null,
    val fecha_actualizacion: String = "",
    val segunda_fecha_vencimiento: String? = null,
    val checkout_url: String? = null,
    val email_pagador: String? = null
)
