package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.ImageUrl

/** コーヒー豆の画像を表すエンティティ。 */
data class CoffeeBeanImage(
    val id: CoffeeBeanImageId,
    val type: CoffeeBeanImageType,
    val imageUrl: ImageUrl,
)
