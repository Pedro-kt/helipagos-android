package com.pedro.helipagospayment.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pedro.helipagospayment.features.paymentrequests.ui.create.CreatePaymentScreen
import com.pedro.helipagospayment.features.paymentrequests.ui.detail.PaymentDetailScreen
import com.pedro.helipagospayment.features.paymentrequests.ui.list.PaymentRequestsScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.PAYMENT_LIST
    ) {
        composable(Destinations.PAYMENT_LIST) {
            PaymentRequestsScreen(
                onPaymentClick = { paymentId ->
                    navController.navigate(Destinations.paymentDetail(paymentId))
                },
                onClickButton = {
                    navController.navigate(Destinations.PAYMENT_CREATE)
                }
            )
        }
        composable(
            route = Destinations.PAYMENT_DETAIL,
            arguments = listOf(
                navArgument("paymentId") {
                    type = NavType.IntType
                }
            )
        ) {
            PaymentDetailScreen()
        }
        composable(
            route = Destinations.PAYMENT_CREATE
        ) {
            CreatePaymentScreen(navController = navController)
        }
    }
}