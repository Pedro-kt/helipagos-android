package com.pedro.helipagospayment.features.paymentrequests.domain.model

data class PaymentResponse(
    val idSp: Int,
    val estadoPago: String,
    val fechaPago: String,
    val medioPago: String,
    val importePagado: Double,
    val descripcion: String,
    val importe: Double,
    val referenciaExterna: String,
    val codigoBarra: String,
    val fechaAcreditacion: String,
    val referenciaExterna2: String,
    val fechaVencimiento: String,
    val importeVencido: Double,
    val fechaCreacion: String,
    val cuotas: Int,
    val fechaActualizacion: String,
    val segundaFechaVencimiento: String,
    val checkoutUrl: String,
    val emailPagador: String
)
