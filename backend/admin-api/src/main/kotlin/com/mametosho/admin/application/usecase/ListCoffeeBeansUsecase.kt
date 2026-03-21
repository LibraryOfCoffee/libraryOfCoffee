package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.query.CoffeeBeanQueryService
import com.mametosho.admin.application.query.result.CoffeeBeanListResult
import com.mametosho.admin.application.query.result.PagedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListCoffeeBeansUsecase(
    private val coffeeBeanQueryService: CoffeeBeanQueryService,
) {
    @Transactional(readOnly = true)
    open fun execute(page: Int, size: Int): PagedResult<CoffeeBeanListResult> {
        return coffeeBeanQueryService.findList(page, size)
    }
}
