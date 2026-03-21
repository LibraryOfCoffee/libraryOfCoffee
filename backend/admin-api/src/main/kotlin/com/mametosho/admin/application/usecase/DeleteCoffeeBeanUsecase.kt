package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.repository.CoffeeBeanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteCoffeeBeanUsecase(
    private val coffeeBeanRepository: CoffeeBeanRepository,
) {
    @Transactional
    open fun execute(id: String): Boolean {
        val existingBean = coffeeBeanRepository.findById(CoffeeBeanId(id)) ?: return false
        coffeeBeanRepository.deleteById(existingBean.id)
        return true
    }
}
