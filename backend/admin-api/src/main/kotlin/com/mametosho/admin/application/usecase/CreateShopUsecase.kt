package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateShopUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional
    fun execute(request: CreateShopRequest): Shop {
        val shop = Shop.create(
            shopifyShopId = request.shopifyShopId,
            name = request.name,
            introduction = request.introduction,
            particular = request.particular,
            images = request.images.map { it.type to it.imageUrl },
        )
        shopRepository.save(shop)
        return shop
    }
}
