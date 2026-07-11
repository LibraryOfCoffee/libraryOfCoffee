package com.mametosho.cs.application.query

import com.mametosho.cs.application.query.result.CoffeeBeanSummaryResult
import com.mametosho.cs.application.result.PagedResult
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.Prefecture

interface CoffeeBeanQueryService {
    fun findList(
        page: Int,
        size: Int,
        origin: String?,
        roastLevel: RoastLevel?,
        prefecture: Prefecture?,
    ): PagedResult<CoffeeBeanSummaryResult>
}
