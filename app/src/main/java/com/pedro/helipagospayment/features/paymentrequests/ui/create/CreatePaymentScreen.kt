package com.pedro.helipagospayment.features.paymentrequests.ui.create

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pedro.helipagospayment.common.ui.components.Loader
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.navigation.Destinations
import java.text.NumberFormat

@Composable
fun CreatePaymentScreen(
    viewModel: CreatePaymentViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    var importe by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var fechaVto by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(
                Unit
            ) {
                detectTapGestures(
                    onTap = {offset ->
                        focusManager.clearFocus()
                    }
                )
            },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = importe,
                onValueChange = { newValue ->
                    val digitsOnly = newValue.filter { it.isDigit() } // esto permite solo digitos!
                    if (digitsOnly.length <= 12) {
                        importe = digitsOnly
                    }
                },
                label = { Text("Importe") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    val amount = importe.toLongOrNull() ?: 0L
                    val formattedAmount = NumberFormat.getCurrencyInstance().format(amount / 100.0)
                    Text("Monto a cobrar: $formattedAmount")
                },
                maxLines = 1
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Mínimo 5 caracteres") }
            )

            OutlinedTextField(
                value = referencia,
                onValueChange = { referencia = it },
                label = { Text("Referencia externa") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Debe ser única") }
            )

            OutlinedTextField(
                value = fechaVto,
                onValueChange = { fechaVto = it },
                label = { Text("Fecha de vencimiento (aaaa-mm-dd)") },
                placeholder = { Text("2025-12-31") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val importeEnCentavos = importe.toIntOrNull() ?: 0
                    val fecha = fechaVto.ifEmpty { "2025-12-31" }

                    val request = CreatePaymentRequestDto(
                        importe = importeEnCentavos,
                        fechaVencimiento = fecha,
                        descripcion = descripcion,
                        referenciaExterna = referencia,
                        urlRedirect = "https://example.com/success",
                        webhook = "https://example.com/webhook"
                    )
                    viewModel.createPayment(request)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = importe.isNotEmpty() &&
                        descripcion.length >= 5 &&
                        referencia.isNotEmpty()
            ) {
                Text("Crear Pago")
            }

            when (val ui = state) {
                is CreatePaymentUiState.Loading -> {
                    Loader()
                }
                is CreatePaymentUiState.Success -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "✅ Pago creado correctamente",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("ID: ${ui.data.idSp}")
                            Text("Estado: ${ui.data.estado}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    navController.navigate(Destinations.paymentDetail(ui.data.idSp)) {
                                        popUpTo(Destinations.PAYMENT_CREATE) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Ver detalle")
                            }
                        }
                    }
                }
                is CreatePaymentUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "❌ Error al crear pago",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(ui.message)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.createPayment(
                                CreatePaymentRequestDto(
                                    importe = importe.toIntOrNull() ?: 0,
                                    fechaVencimiento = fechaVto.ifEmpty { "2025-12-31" },
                                    descripcion = descripcion,
                                    referenciaExterna = referencia,
                                    urlRedirect = "https://example.com/success",
                                    webhook = "https://example.com/webhook"
                                )
                            )}) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}