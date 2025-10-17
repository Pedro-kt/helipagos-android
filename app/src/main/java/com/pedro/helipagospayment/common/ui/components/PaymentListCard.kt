package com.pedro.helipagospayment.common.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto

@Composable
fun PaymentItemCard(
    payment: PaymentResponseDto,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = {
            onClick()
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = payment.descripcion,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estado: ${payment.estado_pago ?: "Desconocido"}")
            Text(text = "Importe: $${payment.importe}")
            Text(text = "Medio: ${payment.medio_pago ?: "N/A"}")

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${payment.fecha_pago ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}