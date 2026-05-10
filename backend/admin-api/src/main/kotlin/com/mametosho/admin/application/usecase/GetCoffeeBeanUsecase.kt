package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.query.CoffeeBeanQueryService
import com.mametosho.admin.application.query.result.CoffeeBeanDetailResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCoffeeBeanUsecase(
    private val coffeeBeanQueryService: CoffeeBeanQueryService,
) {
    @Transactional(readOnly = true)
    open fun execute(id: String): CoffeeBeanDetailResult? {
        return coffeeBeanQueryService.findDetail(id)
    }
}
