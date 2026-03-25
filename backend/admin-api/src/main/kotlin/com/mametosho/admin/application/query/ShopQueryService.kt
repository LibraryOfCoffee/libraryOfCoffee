package com.mametosho.admin.application.query

import com.mametosho.admin.application.query.result.PagedResult
import com.mametosho.admin.application.query.result.ShopListResult

interface ShopQueryService {
    fun findList(page: Int, size: Int, name: String? = null): PagedResult<ShopListResult>
}
