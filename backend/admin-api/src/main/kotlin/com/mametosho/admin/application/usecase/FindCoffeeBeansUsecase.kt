package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.query.CoffeeBeanQueryService
import com.mametosho.admin.application.query.result.CoffeeBeanSummaryResult
import com.mametosho.admin.application.result.PagedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindCoffeeBeansUsecase(
    private val coffeeBeanQueryService: CoffeeBeanQueryService,
) {
    @Transactional(readOnly = true)
    open fun execute(page: Int, size: Int): PagedResult<CoffeeBeanSummaryResult> {
        return coffeeBeanQueryService.findList(page, size)
    }
}
