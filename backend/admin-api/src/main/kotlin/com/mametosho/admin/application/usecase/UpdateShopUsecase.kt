package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.UpdateShopRequest
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateShopUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional
    fun execute(id: String, request: UpdateShopRequest): Shop? {
        val shopId = ShopId(id)
        val existingShop = shopRepository.findById(shopId) ?: return null
        val updatedShop = existingShop.update(
            shopifyShopId = request.shopifyShopId,
            name = request.name,
            introduction = request.introduction,
            particular = request.particular,
            images = request.images.map { it.type to it.imageUrl },
        )
        shopRepository.save(updatedShop)
        return updatedShop
    }
}
