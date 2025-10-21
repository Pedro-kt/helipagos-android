package com.pedro.helipagospayment.features.paymentrequests.domain.model

data class PaymentPaged(
    val idSp: Int,
    val codigoBarra: String,
    val estadoPago: String,
    val medioPago: String,
    val descripcion: String,
    val importePagado: String,
    val referenciaExterna: String,
    val referenciaExternaTwo: String,
    val fechaCreacion: String,
    val fechaPago: String,
    val fechaContracargo: String,
    val fechaVencimiento: String,
    val segundaFechaVencimiento: String
)
