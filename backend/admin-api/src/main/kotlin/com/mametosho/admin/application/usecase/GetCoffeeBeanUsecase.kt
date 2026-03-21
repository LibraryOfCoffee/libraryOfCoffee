package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.repository.CoffeeBeanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCoffeeBeanUsecase(
    private val coffeeBeanRepository: CoffeeBeanRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(id: String): CoffeeBean? {
        return coffeeBeanRepository.findById(CoffeeBeanId(id))
    }
}
