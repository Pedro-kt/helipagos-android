package com.pedro.helipagospayment.features.paymentrequests.data.mapper

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentPagedDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged

fun PaymentPagedDto.toPaymentPaged() = PaymentPaged(
    idSp = idSp,
    codigoBarra = codigoBarra ?: "No disponible",
    estadoPago = estadoPago ?: "No disponible",
    medioPago = medioPago ?: "Medio de pago no disponible",
    descripcion = descripcion ?: "No disponible",
    importePagado = importePagado ?: "0.0",
    referenciaExterna = referenciaExterna ?: "No disponible",
    referenciaExternaTwo = referenciaExternaTwo ?: "No disponible",
    fechaCreacion = fechaCreacion ?: "Sin fecha",
    fechaPago = fechaPago ?: "Sin fecha",
    fechaContracargo = fechaContracargo ?: "Sin fecha",
    fechaVencimiento = fechaVencimiento ?: "Sin fecha",
    segundaFechaVencimiento = segundaFechaVencimiento ?: "Sin fecha"
)