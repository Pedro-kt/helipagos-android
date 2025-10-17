package com.pedro.helipagospayment.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pedro.helipagospayment.navigation.AppNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Helipagos Payment") }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(Icons.Default.Menu, contentDescription = "Lista de pagos")
                    },
                    label = { Text("Actividad") },
                    selected = true,
                    onClick = {

                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation()
        }
    }
}