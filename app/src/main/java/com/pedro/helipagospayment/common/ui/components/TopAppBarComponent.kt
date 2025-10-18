package com.pedro.helipagospayment.common.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
                title = { Text("Actividad") }
            )
        }
        Destinations.PAYMENT_DETAIL -> {
            TopAppBar(
                title = { Text("Detalle del pago") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "volver atras")
                    }
                }
            )
        }
        Destinations.PAYMENT_CREATE -> {
            TopAppBar(
                title = { Text("Crear Solicitud de Pago") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "volver atras")
                    }
                }
            )
        }
    }

}