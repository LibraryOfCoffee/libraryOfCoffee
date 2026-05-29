package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.uploadImages
import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.domain.service.ImageStorageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class CreateCoffeeBeanUsecase(
    private val coffeeBeanRepository: CoffeeBeanRepository,
    private val imageStorageService: ImageStorageService,
) {
    @Transactional
    open fun execute(
        request: CreateCoffeeBeanRequest,
        imageFiles: List<MultipartFile>,
        imageTypes: List<String>,
    ): CoffeeBean {
        val coffeeBeanId = UUID.randomUUID().toString()

        val images = imageStorageService.uploadImages(
            prefix = "coffee-beans",
            entityId = coffeeBeanId,
            imageFiles = imageFiles,
            imageTypes = imageTypes,
        )

        val tastes = request.tastes.map { tasteRequest ->
            tasteRequest.tasteId to tasteRequest.evaluationValue
        }

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
            images = images,
            tastes = tastes,
            id = coffeeBeanId,
        )
        coffeeBeanRepository.save(coffeeBean)
        return coffeeBean
    }
}
