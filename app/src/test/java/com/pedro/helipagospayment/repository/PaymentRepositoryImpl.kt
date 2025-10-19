package com.pedro.helipagospayment.repository

import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentRequestDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.CreatePaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.repository.PaymentRepository
import com.pedro.helipagospayment.features.paymentrequests.data.repository.PaymentRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentRepositoryImplTest {

    private val api: PaymentApi = mockk()
    private lateinit var repository: PaymentRepository

    @Before
    fun setup() {
        repository = PaymentRepositoryImpl(api)
    }

    // metodo getPaymentDetail

    @Test
    fun `getPaymentDetail devuelve resultado exitoso cuando hay datos`() = runTest {
        // given
        val paymentId = 1
        val fakePayment = createFakePayment(paymentId)
        coEvery { api.getPaymentDetail(paymentId) } returns listOf(fakePayment)

        // when
        val result = repository.getPaymentDetail(paymentId)

        // then
        assertTrue(result.isSuccess)
        result.onSuccess { payment ->
            assertEquals(paymentId, payment.idSp)
            assertEquals("Pago test $paymentId", payment.descripcion)
        }
        coVerify(exactly = 1) { api.getPaymentDetail(paymentId) }
    }

    @Test
    fun `getPaymentDetail devuelve error cuando lista esta vacia`() = runTest {
        // given
        val paymentId = 999
        coEvery { api.getPaymentDetail(paymentId) } returns emptyList()

        // when
        val result = repository.getPaymentDetail(paymentId)

        // then
        assertTrue(result.isFailure)
        assertEquals("Payment not found", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { api.getPaymentDetail(paymentId) }
    }

    @Test
    fun `getPaymentDetail devuelve error cuando API falla`() = runTest {
        // given
        val paymentId = 1
        val exception = Exception("Network error")
        coEvery { api.getPaymentDetail(paymentId) } throws exception

        // when
        val result = repository.getPaymentDetail(paymentId)

        // then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { api.getPaymentDetail(paymentId) }
    }

    // metodo createPayment

    @Test
    fun `createPayment devuelve resultado exitoso`() = runTest {
        // given
        val request = createFakeCreateRequest()
        val response = CreatePaymentResponseDto(
            idSp = 1,
            idCliente = 1,
            estado = "GENERADA",
            referenciaExterna = "1933810",
            fechaCreacion = "2025-10-19",
            descripcion = "Test payment",
            codigoBarra = "123456789",
            idUrl = "abc123",
            checkoutUrl = "https://checkout.example.com",
            shortUrl = "https://short.url",
            fechaVencimiento = "2025-12-31",
            importe = 10000
        )
        coEvery { api.createPayment(request) } returns response

        // when
        val result = repository.createPayment(request)

        // then
        assertTrue(result.isSuccess)
        result.onSuccess { created ->
            assertEquals(1, created.idSp)
            assertEquals("GENERADA", created.estado)
            assertEquals("1933810", created.referenciaExterna)
        }
        coVerify(exactly = 1) { api.createPayment(request) }
    }

    @Test
    fun `createPayment devuelve error cuando API falla`() = runTest {
        // given
        val request = createFakeCreateRequest()
        val exception = Exception("Server error 500")
        coEvery { api.createPayment(request) } throws exception

        // when
        val result = repository.createPayment(request)

        // then
        assertTrue(result.isFailure)
        assertEquals("Server error 500", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { api.createPayment(request) }
    }

    // metodo getPaymentsPaged

    @Test
    fun `getPaymentsPaged devuelve Flow de PagingData`() = runTest {
        // given
        val fakePayments = listOf(createFakePayment(1), createFakePayment(2))
        coEvery { api.getPayments() } returns fakePayments

        // when
        val pagingDataFlow = repository.getPaymentsPaged()

        // then
        assertNotNull(pagingDataFlow)
        // el Flow existe y puede ser observado
    }

    // auxiliares para crear objetos falsos

    private fun createFakePayment(id: Int) = PaymentResponseDto(
        idSp = id,
        descripcion = "Pago test $id",
        estadoPago = "GENERADA",
        importe = 100.0 * id,
        medioPago = null,
        fechaPago = null,
        fechaCreacion = "2025-10-19",
        fechaVencimiento = "2025-12-31",
        referenciaExterna = "REF-$id",
        codigoBarra = null,
        fechaAcreditacion = null,
        referenciaExterna2 = null,
        importeVencido = null,
        cuotas = null,
        fechaActualizacion = "2025-10-19",
        segundaFechaVencimiento = null,
        checkoutUrl = null,
        emailPagador = null,
        importePagado = null
    )

    private fun createFakeCreateRequest(ref: String = "REF123") = CreatePaymentRequestDto(
        importe = 10000,
        descripcion = "Test payment",
        referenciaExterna = ref,
        fechaVencimiento = "2025-12-31",
        urlRedirect = "https://example.com/success",
        webhook = "https://example.com/webhook"
    )
}
