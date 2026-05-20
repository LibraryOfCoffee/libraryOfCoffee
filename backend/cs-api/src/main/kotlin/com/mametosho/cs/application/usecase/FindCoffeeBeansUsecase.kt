package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.CoffeeBeanQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.cs.application.query.result.PagedResult
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
    ): PagedResult<CoffeeBeanListResult> {
        return coffeeBeanQueryService.findList(page, size, origin, roastLevel, prefecture)
    }
}
