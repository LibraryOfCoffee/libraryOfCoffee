package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.CoffeeListGroupQueryService
import com.mametosho.cs.application.query.result.CoffeeListGroupDetailResult
import org.springframework.stereotype.Service

@Service
class FindCoffeeListGroupDetailUsecase(
    private val coffeeListGroupQueryService: CoffeeListGroupQueryService,
) {
    fun execute(id: String): CoffeeListGroupDetailResult? {
        return coffeeListGroupQueryService.findDetailById(id)
    }
}
