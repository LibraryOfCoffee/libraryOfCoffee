package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.deleteImages
import com.mametosho.admin.application.service.uploadImages
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
    ): CoffeeBean? {
        val existingBean = coffeeBeanRepository.findById(CoffeeBeanId(id)) ?: return null

        val oldImageUrls = existingBean.images.map { it.imageUrl.value }
        val images = if (imageFiles.isNotEmpty()) {
            imageStorageService.uploadImages(
                prefix = "coffee-beans",
                entityId = id,
                imageFiles = imageFiles,
                imageTypes = imageTypes,
            )
        } else {
            existingBean.images.map { it.type.name to it.imageUrl.value }
        }

        val tastes = request.tastes.map { tasteRequest ->
            tasteRequest.tasteId to tasteRequest.evaluationValue
        }

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
            images = images,
            tastes = tastes,
        )
        coffeeBeanRepository.save(updatedBean)

        if (imageFiles.isNotEmpty()) {
            imageStorageService.deleteImages(oldImageUrls)
        }

        return updatedBean
    }
}
