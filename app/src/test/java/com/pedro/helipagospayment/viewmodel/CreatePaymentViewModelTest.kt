package com.pedro.helipagospayment.viewmodel

import app.cash.turbine.test
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.CreatePaymentUseCase
import com.pedro.helipagospayment.features.paymentrequests.ui.create.CreatePaymentUiState
import com.pedro.helipagospayment.features.paymentrequests.ui.create.CreatePaymentViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePaymentViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var createPaymentUseCase: CreatePaymentUseCase
    private lateinit var viewModel: CreatePaymentViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        createPaymentUseCase = mockk()
        viewModel = CreatePaymentViewModel(createPaymentUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando createPayment es exitoso emite Loading y luego Success`() = runTest {
        val request = CreatePaymentRequestDto(
            importe = 10000,
            fechaVencimiento = "2025-12-31",
            descripcion = "TEST",
            referenciaExterna = "1239842",
            urlRedirect = "https://www.helipagos.com",
            webhook = "https://www.helipagos.com"
        )

        val response = CreatePaymentResponseDto(
            idSp = 12039,
            idCliente = 1239842,
            estado = "Pendiente",
            referenciaExterna = "1239842",
            fechaCreacion = "2025-12-25",
            descripcion = "TEST",
            codigoBarra = "10349049134345",
            idUrl = "1239842",
            checkoutUrl = "https://www.helipagos.com",
            shortUrl = "https://www.helipagos.com",
            fechaVencimiento = "2025-12-31",
            importe = 294444
        )

        coEvery { createPaymentUseCase(request) } returns Result.success(response)

        viewModel.uiState.test {
            viewModel.createPayment(request)

            assert(awaitItem() is CreatePaymentUiState.Idle)
            assert(awaitItem() is CreatePaymentUiState.Loading)
            val success = awaitItem()
            assert(success is CreatePaymentUiState.Success)
            assert((success as CreatePaymentUiState.Success).data == response)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cuando createPayment falla emite Loading y luego Error`() = runTest {
        val request = CreatePaymentRequestDto(
            importe = 10000,
            fechaVencimiento = "2025-12-31",
            descripcion = "TEST",
            referenciaExterna = "1239842",
            urlRedirect = "https://www.helipagos.com",
            webhook = "https://www.helipagos.com"
        )

        val exception = RuntimeException("Error del servidor")
        coEvery { createPaymentUseCase(request) } returns Result.failure(exception)

        viewModel.uiState.test {
            viewModel.createPayment(request)

            assert(awaitItem() is CreatePaymentUiState.Idle)
            assert(awaitItem() is CreatePaymentUiState.Loading)
            val error = awaitItem()
            assert(error is CreatePaymentUiState.Error)
            assert((error as CreatePaymentUiState.Error).message == "Error del servidor")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cuando se llama createPayment nuevamente cancela la request anterior`() = runTest {
        val request1 = CreatePaymentRequestDto(
            importe = 1000, fechaVencimiento = "2025-12-31",
            descripcion = "TEST1", referenciaExterna = "1",
            urlRedirect = "", webhook = ""
        )
        val request2 = request1.copy(descripcion = "TEST2")

        val response1 = CreatePaymentResponseDto(
            idSp = 1, idCliente = 1, estado = "Pendiente",
            referenciaExterna = "1", fechaCreacion = "2025-12-01", descripcion = "TEST1",
            codigoBarra = "", idUrl = "", checkoutUrl = "", shortUrl = "", fechaVencimiento = "", importe = 1000
        )
        val response2 = response1.copy(descripcion = "TEST2")

        coEvery { createPaymentUseCase(request1) } coAnswers {
            delay(1000)
            Result.success(response1)
        }
        coEvery { createPaymentUseCase(request2) } returns Result.success(response2)

        viewModel.uiState.test {
            viewModel.createPayment(request1)
            advanceTimeBy(100)
            viewModel.createPayment(request2)

            assert(awaitItem() is CreatePaymentUiState.Idle)
            assert(awaitItem() is CreatePaymentUiState.Loading)
            val success = awaitItem()
            assert(success is CreatePaymentUiState.Success)
            assert((success as CreatePaymentUiState.Success).data.descripcion == "TEST2")

            cancelAndIgnoreRemainingEvents()
        }
    }
}

