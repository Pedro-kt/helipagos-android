package com.pedro.helipagospayment.paging

import androidx.paging.PagingSource
import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.paging.PaymentPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentPagingSourceTest {

    private val api: PaymentApi = mockk()
    private lateinit var pagingSource: PaymentPagingSource

    private val samplePayments = List(25) { index ->
        PaymentResponseDto(
            idSp = index + 1,
            descripcion = "Pago $index",
            estadoPago = "Pendiente",
            importe = 100.00,
            medioPago = "Tarjeta",
            fechaPago = "2025-12-31"
        )
    }

    @Before
    fun setup() {
        pagingSource = PaymentPagingSource(api)
    }

    @Test
    fun `load devuelve primera pagina correctamente`() = runTest {
        // given
        coEvery { api.getPayments() } returns samplePayments

        // when
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // then
        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assert(page.data.size == 10)
        assert(page.data.first().idSp == 1)
        assert(page.nextKey == 1)
        assert(page.prevKey == null)
    }

    @Test
    fun `load devuelve ultima pagina correctamente`() = runTest {
        // given
        coEvery { api.getPayments() } returns samplePayments

        // when
        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 2,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // then
        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        // Verifico que queden los 5 items sobrantes
        assert(page.data.size == 5)
        assert(page.data.first().idSp == 21)
        assert(page.nextKey == null)
        assert(page.prevKey == 1)
    }

    @Test
    fun `load devuelve error cuando API falla`() = runTest {
        val exception = Exception("Network error")

        // given
        coEvery { api.getPayments() } throws exception

        // when
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // then
        assert(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals("Network error", error.throwable.message)
    }

    @Test
    fun `load usa cache y no llama API multiples veces`() = runTest {
        // given
        coEvery { api.getPayments() } returns samplePayments

        // when
        pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 10, placeholdersEnabled = false)
        )
        pagingSource.load(
            PagingSource.LoadParams.Append(key = 1, loadSize = 10, placeholdersEnabled = false)
        )

        // Then
        // verifico que la api se llama una vez usando exactly = 1, lo que verifica que usa el cache
        coVerify(exactly = 1) { api.getPayments() }
    }

    @Test
    fun `clearCache fuerza recarga desde API`() = runTest {
        // given
        coEvery { api.getPayments() } returns samplePayments

        // when
        pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 10, placeholdersEnabled = false)
        )

        pagingSource.clearCache()

        pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 10, placeholdersEnabled = false)
        )

        // then - API se llama dos veces (sin cache después de clear)
        coVerify(exactly = 2) { api.getPayments() }
    }

    @Test
    fun `load devuelve lista vacia cuando no hay mas items`() = runTest {
        // given
        coEvery { api.getPayments() } returns samplePayments

        // when
        pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 10, placeholdersEnabled = false)
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 10, // esta pagina no existe
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertTrue(page.data.isEmpty())
        assertNull(page.nextKey)
    }

}