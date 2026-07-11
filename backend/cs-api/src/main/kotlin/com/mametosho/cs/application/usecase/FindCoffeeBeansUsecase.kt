package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.CoffeeBeanQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanSummaryResult
import com.mametosho.cs.application.result.PagedResult
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.Prefecture
import org.springframework.stereotype.Service

@Service
class FindCoffeeBeansUsecase(
    private val coffeeBeanQueryService: CoffeeBeanQueryService,
) {
    fun execute(
        page: Int,
        size: Int,
        origin: String?,
        roastLevel: RoastLevel?,
        prefecture: Prefecture?,
    ): PagedResult<CoffeeBeanSummaryResult> {
        return coffeeBeanQueryService.findList(page, size, origin, roastLevel, prefecture)
    }
}
