package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateShopUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional
    fun execute(request: CreateShopRequest): Shop {
        val shop = Shop(
            id = ShopId(UUID.randomUUID().toString()),
            shopifyShopId = ShopifyShopId(request.shopifyShopId),
            name = request.name,
            introduction = request.introduction,
            particular = request.particular,
            images = request.images.map { image ->
                ShopImage(
                    id = ShopImageId(UUID.randomUUID().toString()),
                    type = ShopImageType.valueOf(image.type),
                    imageUrl = ImageUrl(image.imageUrl),
                )
            },
        )
        shopRepository.save(shop)
        return shop
    }
}
