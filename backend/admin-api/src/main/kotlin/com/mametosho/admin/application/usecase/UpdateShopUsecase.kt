package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.deleteImages
import com.mametosho.admin.application.service.uploadImages
import com.mametosho.admin.presentation.dto.request.UpdateShopRequest
import com.mametosho.domain.model.shared.ParticipationStatus
import com.mametosho.domain.model.shop.Prefecture
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.ShopRepository
import com.mametosho.domain.service.ImageStorageService
import com.mametosho.domain.service.ShopDropDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class UpdateShopUsecase(
    private val shopRepository: ShopRepository,
    private val shopDropDomainService: ShopDropDomainService,
    private val imageStorageService: ImageStorageService,
) {
    @Transactional
    open fun execute(
        id: String,
        request: UpdateShopRequest,
        imageFiles: List<MultipartFile>,
        imageTypes: List<String>,
    ): Shop? {
        val shopId = ShopId(id)
        val existingShop = shopRepository.findById(shopId) ?: return null

        val oldImageUrls = existingShop.images.map { it.imageUrl.value }
        val images = if (imageFiles.isNotEmpty()) {
            imageStorageService.uploadImages(
                prefix = "shops",
                entityId = id,
                imageFiles = imageFiles,
                imageTypes = imageTypes,
            )
        } else {
            existingShop.images.map { it.type.name to it.imageUrl.value }
        }

        val updatedShop = existingShop.update(
            shopifyShopId = request.shopifyShopId,
            name = request.name,
            introduction = request.introduction,
            particular = request.particular,
            shopUrl = request.shopUrl,
            prefecture = Prefecture.valueOf(request.prefecture),
            participationStatus = request.participationStatus,
            images = images,
        )
        shopRepository.save(updatedShop)

        if (updatedShop.participationStatus == ParticipationStatus.DROPPED) {
            shopDropDomainService.invalidateCoffeeBeans(shopId)
        }

        if (imageFiles.isNotEmpty()) {
            imageStorageService.deleteImages(oldImageUrls)
        }

        return updatedShop
    }
}
