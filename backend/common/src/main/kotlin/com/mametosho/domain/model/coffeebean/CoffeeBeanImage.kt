package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.ImageUrl

/**
 * コーヒー豆の画像を表すエンティティ。
 *
 * @property id 画像ID
 * @property type 画像の種別
 * @property imageUrl 画像のURL
 */
data class CoffeeBeanImage(
    val id: CoffeeBeanImageId,
    val type: CoffeeBeanImageType,
    val imageUrl: ImageUrl,
)
