package com.pedro.helipagospayment.features.paymentrequests.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentsPagedResponseDto(
    @SerialName("content") val content: List<PaymentResponseDto>,
    @SerialName("pageable") val pageable: PageableDto,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("last") val last: Boolean,
    @SerialName("size") val size: Int,
    @SerialName("number") val number: Int,
    @SerialName("sort") val sort: SortDto,
    @SerialName("numberOfElements") val numberOfElements: Int,
    @SerialName("first") val first: Boolean,
    @SerialName("empty") val empty: Boolean
)

@Serializable
data class PageableDto(
    @SerialName("sort") val sort: SortDto,
    @SerialName("offset") val offset: Long,
    @SerialName("pageNumber") val pageNumber: Int,
    @SerialName("pageSize") val pageSize: Int,
    @SerialName("paged") val paged: Boolean,
    @SerialName("unpaged") val unpaged: Boolean
)

@Serializable
data class SortDto(
    @SerialName("sorted") val sorted: Boolean,
    @SerialName("unsorted") val unsorted: Boolean,
    @SerialName("empty") val empty: Boolean
)