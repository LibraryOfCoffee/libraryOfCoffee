package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.ImageUrl

/** 店舗画像を表すエンティティ。 */
data class ShopImage(
    val id: ShopImageId,
    val type: ShopImageType,
    val imageUrl: ImageUrl,
)
