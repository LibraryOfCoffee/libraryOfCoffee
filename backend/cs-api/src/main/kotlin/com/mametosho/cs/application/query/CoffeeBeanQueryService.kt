package com.mametosho.cs.application.query

import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.domain.model.PagedResult
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.Prefecture

interface CoffeeBeanQueryService {
    fun findList(
        page: Int,
        size: Int,
        origin: String?,
        roastLevel: RoastLevel?,
        prefecture: Prefecture?,
    ): PagedResult<CoffeeBeanListResult>
}
