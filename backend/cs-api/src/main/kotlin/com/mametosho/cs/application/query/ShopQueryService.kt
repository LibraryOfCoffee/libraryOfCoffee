package com.mametosho.cs.application.query

import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.cs.application.query.result.ShopListResult

interface ShopQueryService {
    fun findList(page: Int, size: Int): PagedResult<ShopListResult>
}
