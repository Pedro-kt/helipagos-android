package com.pedro.helipagospayment.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.mapper.toPaymentPaged
import com.pedro.helipagospayment.features.paymentrequests.data.model.PageableDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentPagedDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentsPagedResponseDto
import com.pedro.helipagospayment.features.paymentrequests.data.model.SortDto
import com.pedro.helipagospayment.features.paymentrequests.data.paging.PaymentPagingSource
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged
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

    @Before
    fun setup() {
        pagingSource = PaymentPagingSource(api)
    }

    @Test
    fun `load devuelve primera pagina correctamente`() = runTest {

        val mockResponse = createMockPagedResponse(
            page = 0,
            pageSize = 20,
            totalPages = 5,
            isLast = false
        )
        coEvery { api.getPaymentsPaged(0, 20) } returns mockResponse

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(20, page.data.size)
        assertEquals(0, page.data.first().idSp)
        assertNull(page.prevKey)
        assertEquals(1, page.nextKey)
    }

    @Test
    fun `load devuelve segunda pagina correctamente`() = runTest {

        val mockResponse = createMockPagedResponse(
            page = 1,
            pageSize = 20,
            totalPages = 5,
            isLast = false
        )
        coEvery { api.getPaymentsPaged(1, 20) } returns mockResponse

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(20, page.data.size)
        assertEquals(0, page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load devuelve ultima pagina sin nextKey`() = runTest {

        val mockResponse = createMockPagedResponse(
            page = 4,
            pageSize = 20,
            totalPages = 5,
            isLast = true
        )
        coEvery { api.getPaymentsPaged(4, 20) } returns mockResponse

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 4,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(20, page.data.size)
        assertEquals(3, page.prevKey)
        assertNull(page.nextKey)
    }

    @Test
    fun `load devuelve error cuando API falla`() = runTest {

        val exception = Exception("Network error")
        coEvery { api.getPaymentsPaged(any(), any()) } throws exception

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals("Network error", error.throwable.message)
    }

    @Test
    fun `load maneja correctamente pagina 0 cuando key es null`() = runTest {

        val mockResponse = createMockPagedResponse(
            page = 0,
            pageSize = 20,
            totalPages = 10,
            isLast = false
        )
        coEvery { api.getPaymentsPaged(0, 20) } returns mockResponse

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertNull(page.prevKey)
        assertEquals(1, page.nextKey)
        coVerify { api.getPaymentsPaged(0, 20) }
    }

    @Test
    fun `getRefreshKey retorna clave correcta basada en anchor position`() = runTest {

        val mockPages = listOf(
            PagingSource.LoadResult.Page(
                data = createMockPayments(0, 20),
                prevKey = null,
                nextKey = 1
            )
        )
        val pagingState = PagingState<Int, PaymentPaged>(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = createMockPayments(0, 20).map { it.toPaymentPaged() },
                    prevKey = null,
                    nextKey = 1
                )
            ),
            anchorPosition = 10,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )


        val refreshKey = pagingSource.getRefreshKey(pagingState)

        assertEquals(0, refreshKey)
    }

    @Test
    fun `getRefreshKey retorna null cuando anchorPosition es null`() = runTest {

        val pagingState = PagingState<Int, PaymentPaged>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(pagingState)

        assertNull(refreshKey)
    }

    private fun createMockPagedResponse(
        page: Int,
        pageSize: Int,
        totalPages: Int,
        isLast: Boolean
    ): PaymentsPagedResponseDto {
        val payments = createMockPayments(page * pageSize, pageSize)

        return PaymentsPagedResponseDto(
            content = payments,
            pageable = PageableDto(
                sort = SortDto(sorted = true, unsorted = false, empty = false),
                offset = (page * pageSize).toLong(),
                pageNumber = page,
                pageSize = pageSize,
                paged = true,
                unpaged = false
            ),
            totalPages = totalPages,
            totalElements = (totalPages * pageSize),
            last = isLast,
            size = pageSize,
            number = page,
            sort = SortDto(sorted = true, unsorted = false, empty = false),
            numberOfElements = payments.size,
            first = page == 0,
            empty = false
        )
    }

    private fun createMockPayments(startId: Int, count: Int): List<PaymentPagedDto> {
        return List(count) { index ->
            PaymentPagedDto(
                idSp = startId + index,
                codigoBarra = "",
                estadoPago = "",
                medioPago = "",
                descripcion = "",
                importePagado = "",
                referenciaExterna = "",
                referenciaExternaTwo = "",
                fechaCreacion = "",
                fechaPago = "",
                fechaContracargo = "",
                fechaVencimiento = "",
                segundaFechaVencimiento = "",
            )
        }
    }
}