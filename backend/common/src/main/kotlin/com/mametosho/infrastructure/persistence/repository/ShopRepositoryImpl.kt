package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.shop.Shop
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
        shopMapper.insertShop(
            ShopEntity(
                id = shop.id.value,
                shopifyShopId = shop.shopifyShopId.value,
                name = shop.name,
                introduction = shop.introduction,
                particular = shop.particular,
            ),
        )
        shop.images.forEach { image ->
            shopMapper.insertShopImage(
                ShopImageEntity(
                    id = image.id.value,
                    shopId = shop.id.value,
                    type = image.type.name.lowercase(),
                    imageUrl = image.imageUrl.value,
                ),
            )
        }
    }
}
