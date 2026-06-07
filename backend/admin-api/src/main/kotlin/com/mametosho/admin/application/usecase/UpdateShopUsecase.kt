package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.ExistingImage
import com.mametosho.admin.application.service.resolveImages
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
        keepImageIds: List<String>,
    ): Shop? {
        val shopId = ShopId(id)
        val existingShop = shopRepository.findById(shopId) ?: return null

        val finalImages = imageStorageService.resolveImages(
            existing = existingShop.images.map { ExistingImage(it.id.value, it.type.name, it.image.value) },
            imageFiles = imageFiles,
            imageTypes = imageTypes,
            keepImageIds = keepImageIds,
            prefix = "shops",
            entityId = id,
        )

        val updatedShop = existingShop.update(
            shopifyShopId = request.shopifyShopId,
            name = request.name,
            introduction = request.introduction,
            particular = request.particular,
            shopUrl = request.shopUrl,
            prefecture = Prefecture.valueOf(request.prefecture),
            participationStatus = request.participationStatus,
            images = finalImages,
        )
        shopRepository.save(updatedShop)

        if (updatedShop.participationStatus == ParticipationStatus.DROPPED) {
            shopDropDomainService.invalidateCoffeeBeans(shopId)
        }

        return updatedShop
    }
}
