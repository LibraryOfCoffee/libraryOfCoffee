package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImage
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageType
import com.mametosho.domain.model.coffeebean.CoffeeBeanTaste
import com.mametosho.domain.model.coffeebean.CoffeeBeanTasteId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import com.mametosho.domain.repository.CoffeeBeanRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CreateCoffeeBeanUsecase(
    private val coffeeBeanRepository: CoffeeBeanRepository,
) {
    fun execute(request: CreateCoffeeBeanRequest): CoffeeBean {
        val coffeeBean = CoffeeBean(
            id = CoffeeBeanId(UUID.randomUUID().toString()),
            shopId = ShopId(request.shopId),
            shopifyBeanId = ShopifyBeanId(request.shopifyBeanId),
            name = request.name,
            description = request.description,
            origin = request.origin,
            farm = request.farm,
            roastLevel = RoastLevel.valueOf(request.roastLevel),
            processingMethod = ProcessingMethod.valueOf(request.processingMethod),
            isSpecialty = request.isSpecialty,
            images = request.images.map { image ->
                CoffeeBeanImage(
                    id = CoffeeBeanImageId(UUID.randomUUID().toString()),
                    type = CoffeeBeanImageType.valueOf(image.type),
                    imageUrl = ImageUrl(image.imageUrl),
                )
            },
            tastes = request.tastes.map { taste ->
                CoffeeBeanTaste(
                    id = CoffeeBeanTasteId(UUID.randomUUID().toString()),
                    tasteId = TasteId(taste.tasteId),
                    evaluationValue = taste.evaluationValue,
                )
            },
        )
        coffeeBeanRepository.save(coffeeBean)
        return coffeeBean
    }
}
