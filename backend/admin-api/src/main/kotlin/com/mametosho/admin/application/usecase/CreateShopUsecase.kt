package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.ImageUpload
import com.mametosho.admin.application.service.uploadImages
import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shop.Prefecture
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.repository.ShopRepository
import com.mametosho.domain.service.ImageStorageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateShopUsecase(
    private val shopRepository: ShopRepository,
    private val imageStorageService: ImageStorageService,
) {
    @Transactional
    open fun execute(
        request: CreateShopRequest,
        uploads: List<ImageUpload>,
    ): Shop {
        val shopId = UUID.randomUUID().toString()

        val images = imageStorageService.uploadImages(
            prefix = "shops",
            entityId = shopId,
            uploads = uploads,
        )

        val shop = Shop.create(
            shopifyShopId = request.shopifyShopId,
            name = request.name,
            introduction = request.introduction,
            particular = request.particular,
            shopUrl = request.shopUrl,
            prefecture = Prefecture.valueOf(request.prefecture),
            participationStatus = request.participationStatus,
            images = images,
            id = shopId,
        )
        shopRepository.save(shop)
        return shop
    }
}
