package com.pedro.helipagospayment.repository

import androidx.paging.PagingData
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentRepositoryImplTest {

    private val api: PaymentApi = mockk()
    private lateinit var repository: PaymentRepository

    @Before
    fun setup() {
        repository = PaymentRepositoryImpl(api)
    }

    // Tests para getPaymentDetail

    @Test
    fun `getPaymentDetail devuelve resultado exitoso cuando hay datos`() = runTest {
        // Given
        val paymentId = 679841
        val fakePayment = createFakePayment(paymentId)
        coEvery { api.getPaymentDetail(paymentId) } returns listOf(fakePayment)

        // When
        val result = repository.getPaymentDetail(paymentId)

        // Then
        assertTrue(result.isSuccess)
        result.onSuccess { payment ->
            assertEquals(paymentId, payment.idSp)
            assertEquals("Pago test $paymentId", payment.descripcion)
            assertEquals("GENERADA", payment.estadoPago)
        }
        coVerify(exactly = 1) { api.getPaymentDetail(paymentId) }
    }

    @Test
    fun `getPaymentDetail devuelve error cuando lista esta vacia`() = runTest {
        // Given
        val paymentId = 999999
        coEvery { api.getPaymentDetail(paymentId) } returns emptyList()

        // When
        val result = repository.getPaymentDetail(paymentId)

        // Then
        assertTrue(result.isFailure)
        result.onFailure { exception ->
            assertEquals("Payment not found", exception.message)
        }
        coVerify(exactly = 1) { api.getPaymentDetail(paymentId) }
    }

    @Test
    fun `getPaymentDetail devuelve error cuando API falla con excepcion`() = runTest {
        // Given
        val paymentId = 1
        val exception = IOException("Network timeout")
        coEvery { api.getPaymentDetail(paymentId) } throws exception

        // When
        val result = repository.getPaymentDetail(paymentId)

        // Then
        assertTrue(result.isFailure)
        result.onFailure { error ->
            assertEquals("Network timeout", error.message)
            assertTrue(error is IOException)
        }
        coVerify(exactly = 1) { api.getPaymentDetail(paymentId) }
    }

    // Tests para createPayment

    @Test
    fun `createPayment devuelve resultado exitoso con todos los datos`() = runTest {
        // Given
        val request = createFakeCreateRequest()
        val response = CreatePaymentResponseDto(
            idSp = 679842,
            idCliente = 5000,
            estado = "GENERADA",
            referenciaExterna = "REF-123",
            fechaCreacion = "2025-10-20",
            descripcion = "Test payment",
            codigoBarra = "139000000050000067984200001000025300000123456789",
            idUrl = "abc123",
            checkoutUrl = "https://checkout.helipagos.com/abc123",
            shortUrl = "https://hpg.link/abc123",
            fechaVencimiento = "2025-12-31",
            importe = 10000
        )
        coEvery { api.createPayment(request) } returns response

        // When
        val result = repository.createPayment(request)

        // Then
        assertTrue(result.isSuccess)
        result.onSuccess { created ->
            assertEquals(679842, created.idSp)
            assertEquals("GENERADA", created.estado)
            assertEquals("REF-123", created.referenciaExterna)
            assertEquals(10000, created.importe)
            assertNotNull(created.checkoutUrl)
            assertNotNull(created.codigoBarra)
        }
        coVerify(exactly = 1) { api.createPayment(request) }
    }

    @Test
    fun `createPayment devuelve error cuando API retorna 400`() = runTest {
        // Given
        val request = createFakeCreateRequest()
        val exception = HttpException(
            Response.error<Any>(
                400,
                "Bad request".toResponseBody(null)
            )
        )
        coEvery { api.createPayment(request) } throws exception

        // When
        val result = repository.createPayment(request)

        // Then
        assertTrue(result.isFailure)
        result.onFailure { error ->
            assertTrue(error is HttpException)
        }
        coVerify(exactly = 1) { api.createPayment(request) }
    }

    @Test
    fun `createPayment devuelve error cuando hay problema de red`() = runTest {
        // Given
        val request = createFakeCreateRequest()
        val exception = SocketTimeoutException("Connection timeout")
        coEvery { api.createPayment(request) } throws exception

        // When
        val result = repository.createPayment(request)

        // Then
        assertTrue(result.isFailure)
        result.onFailure { error ->
            assertTrue(error is SocketTimeoutException)
            assertEquals("Connection timeout", error.message)
        }
        coVerify(exactly = 1) { api.createPayment(request) }
    }

    // Tests para getPaymentsPaged

    @Test
    fun `getPaymentsPaged devuelve Flow de PagingData correctamente`() = runTest {
        // When
        val pagingDataFlow = repository.getPaymentsPaged()

        // Then
        assertNotNull(pagingDataFlow)
        // Verificamos que es un Flow válido
        assertTrue(pagingDataFlow is Flow<PagingData<PaymentResponseDto>>)
    }

    // Helpers

    private fun createFakePayment(id: Int) = PaymentResponseDto(
        idSp = id,
        descripcion = "Pago test $id",
        estadoPago = "GENERADA",
        importe = 25300.0,
        medioPago = null,
        fechaPago = null,
        fechaCreacion = "2025-10-20 11:55:57",
        fechaVencimiento = "2025-10-28",
        referenciaExterna = "TEST",
        codigoBarra = "139000000050000067984100001000025300000123456022",
        fechaAcreditacion = null,
        referenciaExterna2 = "TEST",
        importeVencido = null,
        cuotas = null,
        fechaActualizacion = "2025-10-20",
        segundaFechaVencimiento = "2025-10-30",
        checkoutUrl = null,
        emailPagador = null,
        importePagado = null
    )

    private fun createFakeCreateRequest(ref: String = "REF-123") = CreatePaymentRequestDto(
        importe = 10000,
        descripcion = "Test payment",
        referenciaExterna = ref,
        fechaVencimiento = "2025-12-31",
        urlRedirect = "https://example.com/success",
        webhook = "https://example.com/webhook"
    )
}
