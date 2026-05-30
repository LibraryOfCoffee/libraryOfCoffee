package com.mametosho.domain.model

data class PagedResult<T>(
    val items: List<T>,
    val totalCount: Long,
    val page: Int,
    val size: Int,
)
