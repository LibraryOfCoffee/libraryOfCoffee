package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import com.mametosho.domain.repository.ShopRepository
import com.mametosho.infrastructure.persistence.mybatis.entity.ShopEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.ShopImageEntity
import com.mametosho.infrastructure.persistence.mybatis.mapper.ShopMapper
import org.springframework.stereotype.Repository

@Repository
class ShopRepositoryImpl(
    private val shopMapper: ShopMapper,
) : ShopRepository {
    override fun save(shop: Shop) {
        shopMapper.upsertShop(
            ShopEntity(
                id = shop.id.value,
                shopifyShopId = shop.shopifyShopId.value,
                name = shop.name,
                introduction = shop.introduction,
                particular = shop.particular,
            ),
        )
        shopMapper.deleteShopImagesByShopId(shop.id.value)
        shop.images.forEach { image ->
            shopMapper.insertShopImage(
                ShopImageEntity(
                    id = image.id.value,
                    shopId = shop.id.value,
                    type = image.type.name,
                    imageUrl = image.imageUrl.value,
                ),
            )
        }
    }

    override fun deleteById(id: ShopId) {
        shopMapper.deleteShopImagesByShopId(id.value)
        shopMapper.deleteShopById(id.value)
    }

    override fun findById(id: ShopId): Shop? {
        val shopEntity = shopMapper.findShopById(id.value) ?: return null
        val imageEntities = shopMapper.findShopImagesByShopId(id.value)
        return Shop(
            id = ShopId(shopEntity.id),
            shopifyShopId = ShopifyShopId(shopEntity.shopifyShopId),
            name = shopEntity.name,
            introduction = shopEntity.introduction,
            particular = shopEntity.particular,
            images = imageEntities.map { img ->
                ShopImage(
                    id = ShopImageId(img.id),
                    type = ShopImageType.valueOf(img.type),
                    imageUrl = ImageUrl(img.imageUrl),
                )
            },
        )
    }
}
