package com.mametosho.admin.application.query

import com.mametosho.admin.application.query.result.CoffeeBeanDetailResult
import com.mametosho.admin.application.query.result.CoffeeBeanListResult
import com.mametosho.admin.application.result.PagedResult

interface CoffeeBeanQueryService {
    fun findList(page: Int, size: Int): PagedResult<CoffeeBeanListResult>
    fun findDetail(id: String): CoffeeBeanDetailResult?
}
