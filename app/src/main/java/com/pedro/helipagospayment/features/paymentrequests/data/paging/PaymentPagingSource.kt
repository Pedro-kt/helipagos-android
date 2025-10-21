package com.pedro.helipagospayment.features.paymentrequests.data.paging

import androidx.paging.PagingSource
import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import androidx.paging.PagingState
import com.pedro.helipagospayment.features.paymentrequests.data.mapper.toPaymentPaged
import com.pedro.helipagospayment.features.paymentrequests.domain.model.PaymentPaged

class PaymentPagingSource(
    private val api: PaymentApi
) : PagingSource<Int, PaymentPaged>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaymentPaged> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            val response = api.getPaymentsPaged(page, pageSize)
            val data = response.content.map { it.toPaymentPaged() }

            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PaymentPaged>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

