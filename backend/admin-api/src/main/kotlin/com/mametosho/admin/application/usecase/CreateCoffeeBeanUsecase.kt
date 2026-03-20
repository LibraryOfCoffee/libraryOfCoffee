package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.repository.CoffeeBeanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateCoffeeBeanUsecase(
    private val coffeeBeanRepository: CoffeeBeanRepository,
) {
    @Transactional
    fun execute(request: CreateCoffeeBeanRequest): CoffeeBean {
        val coffeeBean = CoffeeBean.create(
            shopId = request.shopId,
            shopifyBeanId = request.shopifyBeanId,
            name = request.name,
            description = request.description,
            origin = request.origin,
            farm = request.farm,
            roastLevel = request.roastLevel,
            processingMethod = request.processingMethod,
            isSpecialty = request.isSpecialty,
            images = request.images.map { it.type to it.imageUrl },
            tastes = request.tastes.map { it.tasteId to it.evaluationValue },
        )
        coffeeBeanRepository.save(coffeeBean)
        return coffeeBean
    }
}
