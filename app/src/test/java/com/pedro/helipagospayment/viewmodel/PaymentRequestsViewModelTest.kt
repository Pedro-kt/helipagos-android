package com.pedro.helipagospayment.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged
import com.pedro.helipagospayment.features.paymentrequests.domain.usecases.GetPaymentsPagedUseCase
import com.pedro.helipagospayment.features.paymentrequests.ui.list.PaymentRequestsViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentRequestsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetPaymentsPagedUseCase
    private lateinit var viewModel: PaymentRequestsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `paymentsPagingData emite PagingData correctamente`() = runTest(testDispatcher) {
        // given
        val fakePayments = listOf(
            createFakePayment(1),
            createFakePayment(2),
            createFakePayment(3)
        )
        val fakePagingData = PagingData.from(fakePayments)
        val fakeFlow = flowOf(fakePagingData)

        every { useCase.invoke() } returns fakeFlow

        // when
        viewModel = PaymentRequestsViewModel(useCase)

        // then
        viewModel.paymentsPagingData.test {
            val item = awaitItem()
            assertNotNull(item)
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { useCase.invoke() }
    }

    @Test
    fun `paymentsPagingData emite datos vacios cuando no hay pagos`() = runTest(testDispatcher) {
        // given
        val emptyPagingData = PagingData.empty<PaymentPaged>()
        val fakeFlow = flowOf(emptyPagingData)

        every { useCase.invoke() } returns fakeFlow

        // when
        viewModel = PaymentRequestsViewModel(useCase)

        // then
        viewModel.paymentsPagingData.test {
            val pagingData = awaitItem()
            assertNotNull(pagingData)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `paymentsPagingData cachea resultados correctamente`() = runTest(testDispatcher) {
        // given
        val fakePagingData = PagingData.from(listOf(createFakePayment(1)))
        val fakeFlow = flowOf(fakePagingData)

        every { useCase.invoke() } returns fakeFlow

        // when
        viewModel = PaymentRequestsViewModel(useCase)

        // Primera colección
        viewModel.paymentsPagingData.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        // Segunda colección
        viewModel.paymentsPagingData.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        // then - cachedIn asegura que solo se llame una vez al useCase
        verify(exactly = 1) { useCase.invoke() }
    }

    @Test
    fun `el ViewModel propaga excepciones del UseCase`() = runTest(testDispatcher) {
        // given
        val exception = RuntimeException("Error del UseCase")
        every { useCase.invoke() } throws exception

        // when & then
        assertThrows(RuntimeException::class.java) {
            PaymentRequestsViewModel(useCase)
        }
    }

    // Auxiliar: crea PaymentPaged para pruebas
    private fun createFakePayment(id: Int) = PaymentPaged(
        idSp = id,
        descripcion = "Pago test $id",
        estadoPago = "GENERADA",
        importePagado = "10000",
        referenciaExterna = "REF-$id",
        fechaCreacion = "2025-10-19",
        fechaVencimiento = "2025-12-31",
        codigoBarra = "",
        segundaFechaVencimiento = "",
        medioPago = "",
        referenciaExternaTwo = "",
        fechaPago = "",
        fechaContracargo = ""
    )
}
