package com.pedro.helipagospayment.common.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto

@Composable
fun PaymentDetailContent(payment: PaymentResponseDto) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {

            Text("ID: ${payment.idSp}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Descripción: ${payment.descripcion}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Importe: $${payment.importe}")

            Text("Estado: ${payment.estadoPago ?: "Generada"}")

            payment.medioPago?.let {
                Text("Medio de pago: $it")
            }

            payment.importePagado?.let {
                Text("Importe pagado: $$it")
            }

            payment.importeVencido?.let {
                Text("Importe vencido: $$it")
            }

            payment.cuotas?.let {
                Text("Cuotas: $it")
            }

            payment.fechaPago?.let {
                Text("Fecha de pago: $it")
            } ?: Text("Fecha de pago: Sin pagar")

            Text("Fecha de creación: ${payment.fechaCreacion}")

            Text("Fecha de actualización: ${payment.fechaActualizacion}")

            Text("Fecha de vencimiento: ${payment.fechaVencimiento}")

            payment.segundaFechaVencimiento?.let {
                Text("Segunda fecha de vencimiento: $it")
            }

            payment.fechaAcreditacion?.let {
                Text("Fecha de acreditación: $it")
            }

            Text("Referencia: ${payment.referenciaExterna}")

            payment.referenciaExterna2?.let {
                Text("Referencia 2: $it")
            }

            payment.codigoBarra?.let {
                Text("Código de barra: $it")
            }

            payment.checkoutUrl?.let {
                Text("Checkout URL: $it")
            }

            payment.emailPagador?.let {
                Text("Email pagador: $it")
            }
        }
    }
}