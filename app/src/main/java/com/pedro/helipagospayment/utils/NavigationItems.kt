package com.pedro.helipagospayment.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import com.pedro.helipagospayment.navigation.Destinations

val navigationItems = listOf(
    BottomNavItem(
        title = "Actividad",
        route = Destinations.PAYMENT_LIST,
        icon = Icons.Default.Menu
    ),
    BottomNavItem(
        title = "Crear Pago",
        route = Destinations.PAYMENT_CREATE,
        icon = Icons.Default.AddCircle
    )
)
