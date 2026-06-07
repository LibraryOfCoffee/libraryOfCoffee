package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.ExistingImage
import com.mametosho.admin.application.service.resolveImages
import com.mametosho.admin.presentation.dto.request.UpdateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.domain.service.ImageStorageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class UpdateCoffeeBeanUsecase(
    private val coffeeBeanRepository: CoffeeBeanRepository,
    private val imageStorageService: ImageStorageService,
) {
    @Transactional
    open fun execute(
        id: String,
        request: UpdateCoffeeBeanRequest,
        imageFiles: List<MultipartFile>,
        imageTypes: List<String>,
        keepImageIds: List<String>,
    ): CoffeeBean? {
        val existingBean = coffeeBeanRepository.findById(CoffeeBeanId(id)) ?: return null

        val finalImages = imageStorageService.resolveImages(
            existing = existingBean.images.map { ExistingImage(it.id.value, it.type.name, it.image.url) },
            imageFiles = imageFiles,
            imageTypes = imageTypes,
            keepImageIds = keepImageIds,
            prefix = "coffee-beans",
            entityId = id,
        )

        val updatedBean = existingBean.update(
            shopId = request.shopId,
            shopifyBeanId = request.shopifyBeanId,
            name = request.name,
            description = request.description,
            origin = request.origin,
            farm = request.farm,
            roastLevel = request.roastLevel,
            processingMethod = request.processingMethod,
            isSpecialty = request.isSpecialty,
            publishStatus = request.publishStatus,
            images = finalImages,
            tastes = request.tastes.map { it.tasteId to it.evaluationValue },
        )
        coffeeBeanRepository.save(updatedBean)
        return updatedBean
    }
}
