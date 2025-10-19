package com.pedro.helipagospayment.features.paymentrequests.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.model.PaymentResponseDto
import javax.inject.Inject

class PaymentPagingSource @Inject constructor(
    private val api: PaymentApi
) : PagingSource<Int, PaymentResponseDto>() {

    /*

    Aclaracion:

        Se implemento **Paging 3** con CACHE EN MEMORIA:
            - Scroll infinito con carga automatica
            - Pull-to-refresh para actualizar datos
            - Cache optimizado para evitar recargas
            - Invalidacion automatica al crear nueva solicitud

        Nota: Debido al no poder consumir el correspondiente enpoint de paginacion,
        se implemento paginacion en memoria. Para produccion con endpoint paginado real,
        se puede migrar facilmente a RemoteMediator + Room para caching persistente.

     */

    private var cachedPayments: List<PaymentResponseDto>? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaymentResponseDto> {
        return try {
            val page = params.key ?: 0
            val pageSize = 10

            // Si no hay cache, cargar todos los datos, esto nos sirve para recargar la screen, al crear una neuva solicitud de pago
            if (cachedPayments == null) {
                cachedPayments = api.getPayments()
            }

            val allPayments = cachedPayments ?: emptyList()

            // Paginar en memoria
            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, allPayments.size)

            val pagedData = if (startIndex < allPayments.size) {
                allPayments.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            LoadResult.Page(
                data = pagedData,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (endIndex >= allPayments.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PaymentResponseDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun clearCache() {
        cachedPayments = null
    }

}