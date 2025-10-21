package com.pedro.helipagospayment.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentResponse
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.GetPaymentDetailUseCase
import com.pedro.helipagospayment.features.paymentrequests.ui.detail.PaymentDetailUiState
import com.pedro.helipagospayment.features.paymentrequests.ui.detail.PaymentDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.Int

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPaymentDetailUseCase: GetPaymentDetailUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getPaymentDetailUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando getPaymentDetailUseCase es exitoso, emite Loading y luego Success`() = runTest {
        val paymentId = 123
        val payment = paymentResponseFake(paymentId)

        coEvery { getPaymentDetailUseCase(paymentId) } returns Result.success(payment)

        val savedStateHandle = SavedStateHandle(mapOf("paymentId" to paymentId))
        val viewModel = PaymentDetailViewModel(getPaymentDetailUseCase, savedStateHandle)

        viewModel.uiState.test {
            val loading = awaitItem()
            assert(loading is PaymentDetailUiState.Loading)

            val success = awaitItem()
            assert(success is PaymentDetailUiState.Success)
            assert((success as PaymentDetailUiState.Success).payment == payment)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cuando getPaymentDetailUseCase falla, emite Loading y luego Error`() = runTest {
        val paymentId = 123
        val exception = RuntimeException("Error del servidor")
        coEvery { getPaymentDetailUseCase(paymentId) } returns Result.failure(exception)

        val savedStateHandle = SavedStateHandle(mapOf("paymentId" to paymentId))
        val viewModel = PaymentDetailViewModel(getPaymentDetailUseCase, savedStateHandle)

        viewModel.uiState.test {
            val loading = awaitItem()
            assert(loading is PaymentDetailUiState.Loading)

            val error = awaitItem()
            assert(error is PaymentDetailUiState.Error)
            assert((error as PaymentDetailUiState.Error).message == "Error del servidor")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `lanza excepcion si paymentId no esta en SavedStateHandle`() {
        val emptyStateHandle = SavedStateHandle()
        PaymentDetailViewModel(getPaymentDetailUseCase, emptyStateHandle)
    }

    @Test
    fun `cuando getPaymentDetailUseCase devuelve exito pero con datos inesperados`() = runTest {
        val paymentId = 1234
        val incompletePayment = paymentResponseFake(paymentId)

        coEvery { getPaymentDetailUseCase(paymentId) } returns Result.success(incompletePayment)

        val savedStateHandle = SavedStateHandle(mapOf("paymentId" to paymentId))
        val viewModel = PaymentDetailViewModel(getPaymentDetailUseCase, savedStateHandle)

        viewModel.uiState.test {
            awaitItem()
            val success = awaitItem()
            assert(success is PaymentDetailUiState.Success)
            assert((success as PaymentDetailUiState.Success).payment == incompletePayment)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

private fun paymentResponseFake(paymentId: Int) = PaymentResponse(
    idSp = 1230,
    estadoPago = "Pendiente",
    fechaPago = "2025-11-30",
    medioPago = "Visa",
    importePagado = 0.0,
    descripcion = "TEST",
    importe = 0.0,
    referenciaExterna = "343930",
    codigoBarra = "644738325",
    fechaAcreditacion = "2025-11-31",
    referenciaExterna2 = "21553",
    fechaVencimiento = "2025-12-30",
    importeVencido = 0.0,
    fechaCreacion = "2025-11-10",
    cuotas = 1,
    fechaActualizacion = "2025-11-20",
    segundaFechaVencimiento = "2025-12-31",
    checkoutUrl = "https://ejemplo.com",
    emailPagador = "email@pagador"
)
