package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.deleteImages
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.ShopRepository
import com.mametosho.domain.service.ImageStorageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteShopUsecase(
    private val shopRepository: ShopRepository,
    private val imageStorageService: ImageStorageService,
) {
    @Transactional
    open fun execute(id: String): Boolean {
        val shopId = ShopId(id)
        val shop = shopRepository.findById(shopId) ?: return false
        imageStorageService.deleteImages(shop.images.map { it.image.value })
        shopRepository.deleteById(shopId)
        return true
    }
}
