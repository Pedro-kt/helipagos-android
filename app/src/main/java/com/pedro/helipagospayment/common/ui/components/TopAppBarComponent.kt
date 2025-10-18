package com.pedro.helipagospayment.common.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.pedro.helipagospayment.navigation.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(
    navController: NavController,
    currentRoute: String?
) {

    val currentRoute = currentRoute

    when (currentRoute) {
         Destinations.PAYMENT_LIST -> {
            TopAppBar(
                title = { Text(
                    text = "Lista de Solicitudes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                ) }
            )
        }
        Destinations.PAYMENT_DETAIL -> {
            TopAppBar(
                title = { Text(
                    text = "Detalle de Solicitud",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "volver atras")
                    }
                }
            )
        }
        Destinations.PAYMENT_CREATE -> {
            TopAppBar(
                title = {
                    Text(
                        text = "Nueva Solicitud",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "volver atras")
                    }
                }
            )
        }
    }

}