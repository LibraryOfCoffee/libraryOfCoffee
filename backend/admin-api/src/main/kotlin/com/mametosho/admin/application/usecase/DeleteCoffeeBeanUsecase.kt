package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.deleteImages
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.domain.service.ImageStorageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteCoffeeBeanUsecase(
    private val coffeeBeanRepository: CoffeeBeanRepository,
    private val imageStorageService: ImageStorageService,
) {
    @Transactional
    open fun execute(id: String): Boolean {
        val existingBean = coffeeBeanRepository.findById(CoffeeBeanId(id)) ?: return false
        imageStorageService.deleteImages(existingBean.images.map { it.image.url })
        coffeeBeanRepository.deleteById(existingBean.id)
        return true
    }
}
