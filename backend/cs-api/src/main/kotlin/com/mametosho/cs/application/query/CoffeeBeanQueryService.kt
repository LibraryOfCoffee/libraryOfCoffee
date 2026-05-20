package com.mametosho.cs.application.query

import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel

interface CoffeeBeanQueryService {
    fun findList(
        page: Int,
        size: Int,
        origin: String?,
        roastLevel: RoastLevel?,
        processingMethod: ProcessingMethod?,
    ): PagedResult<CoffeeBeanListResult>
}
