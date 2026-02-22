package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.ImageUrl

data class CoffeeBeanImage(
    val id: CoffeeBeanImageId,
    val type: CoffeeBeanImageType,
    val imageUrl: ImageUrl,
)
