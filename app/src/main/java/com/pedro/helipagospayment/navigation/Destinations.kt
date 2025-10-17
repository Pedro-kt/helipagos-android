package com.pedro.helipagospayment.navigation

object Destinations {
    const val PAYMENT_LIST = "payment_list"
    const val PAYMENT_DETAIL = "payment_detail/{paymentId}"
    const val PAYMENT_CREATE = "payment_create"

    // funcion helper para construir la ruta de detalle de pago
    fun paymentDetail(paymentId: Int) = "payment_detail/$paymentId"
}