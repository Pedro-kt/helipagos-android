package com.pedro.helipagospayment.features.paymentrequests.data.mapper

import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentResponse

fun PaymentResponseDto.toPaymentResponse(): PaymentResponse {
    return PaymentResponse(
        idSp = idSp,
        estadoPago = estadoPago ?: "Desconocido",
        fechaPago = fechaPago ?: "N/A",
        medioPago = medioPago ?: "N/A",
        importePagado = importePagado ?: 0.0,
        descripcion = descripcion ?: "Sin descripción",
        importe = importe ?: 0.0,
        referenciaExterna = referenciaExterna ?: "N/A",
        codigoBarra = codigoBarra ?: "N/A",
        fechaAcreditacion = fechaAcreditacion ?: "N/A",
        referenciaExterna2 = referenciaExterna2 ?: "N/A",
        fechaVencimiento = fechaVencimiento ?: "N/A",
        importeVencido = importeVencido ?: 0.0,
        fechaCreacion = fechaCreacion ?: "N/A",
        cuotas = cuotas ?: 0,
        fechaActualizacion = fechaActualizacion ?: "N/A",
        segundaFechaVencimiento = segundaFechaVencimiento ?: "N/A",
        checkoutUrl = checkoutUrl ?: "",
        emailPagador = emailPagador ?: "N/A"
    )
}
