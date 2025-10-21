package com.pedro.helipagospayment.features.paymentrequests.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pedro.helipagospayment.common.ui.components.Loader
import com.pedro.helipagospayment.common.ui.components.PaymentItemCard
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentPagedDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentRequestsScreen(
    viewModel: PaymentRequestsViewModel = hiltViewModel(),
    onPaymentClick: (Int) -> Unit,
    onClickButton: () -> Unit
) {
    val paymentsLazyPagingItems = viewModel.paymentsPagingData.collectAsLazyPagingItems()
    var isManualRefreshing by remember { mutableStateOf(false) }

    val isLoading = paymentsLazyPagingItems.loadState.refresh is LoadState.Loading
    val isError = paymentsLazyPagingItems.loadState.refresh is LoadState.Error
    val isEmpty = paymentsLazyPagingItems.itemCount == 0
    val hasLoadedOnce = remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            isManualRefreshing = false
            hasLoadedOnce.value = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = isManualRefreshing,
            onRefresh = {
                isManualRefreshing = true
                paymentsLazyPagingItems.refresh()
            }
        ) {
            when {
                isLoading && isEmpty && !hasLoadedOnce.value -> {
                    Loader()
                }

                isError && !isManualRefreshing -> {
                    ErrorState(
                        onRetry = {
                            isManualRefreshing = true
                            paymentsLazyPagingItems.retry()
                        }
                    )
                }

                !isLoading && isEmpty && hasLoadedOnce.value -> {
                    EmptyState()
                }

                else -> {
                    PaymentList(
                        paymentsLazyPagingItems = paymentsLazyPagingItems,
                        onPaymentClick = onPaymentClick
                    )
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onClickButton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp, end = 16.dp),
            icon = { Icon(Icons.Default.Add, contentDescription = "Crear solicitud") },
            text = { Text("Nueva Solicitud") }
        )
    }
}

@Composable
private fun PaymentList(
    paymentsLazyPagingItems: LazyPagingItems<PaymentPaged>,
    onPaymentClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = paymentsLazyPagingItems.itemCount,
            key = { index -> paymentsLazyPagingItems[index]?.idSp ?: index }
        ) { index ->
            paymentsLazyPagingItems[index]?.let { payment ->
                PaymentItemCard(
                    payment = payment,
                    onClick = { onPaymentClick(payment.idSp) }
                )
            }
        }

        if (paymentsLazyPagingItems.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Cargando más...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (paymentsLazyPagingItems.loadState.append is LoadState.Error) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 100.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Error al cargar más solicitudes, por favor intente nuevamente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                        TextButton(onClick = { paymentsLazyPagingItems.retry() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Error al cargar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Text(
                    text = "Algo a fallado mientras cargabamos las solicitudes de pago, por favor vuelva a intentar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reintentar")
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )

            Text(
                text = "No hay solicitudes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Crea tu primera solicitud de pago\npresionando el botón +",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}