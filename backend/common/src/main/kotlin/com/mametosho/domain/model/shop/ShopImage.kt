package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.ImageUrl

/**
 * 店舗画像を表すエンティティ。
 *
 * @property id 画像ID
 * @property type 画像の種別
 * @property imageUrl 画像のURL
 */
data class ShopImage(
    val id: ShopImageId,
    val type: ShopImageType,
    val imageUrl: ImageUrl,
)
