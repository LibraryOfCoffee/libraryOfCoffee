package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shop.ShopId

data class CoffeeBean(
    val id: CoffeeBeanId,
    val shopId: ShopId,
    val shopifyBeanId: ShopifyBeanId,
    val name: String,
    val description: String,
    val origin: String,
    val farm: String?,
    val roastLevel: RoastLevel,
    val processingMethod: ProcessingMethod,
    val images: List<CoffeeBeanImage>,
    val tastes: List<CoffeeBeanTaste>,
) {
    init {
        val duplicateTasteIds = tastes.groupBy { it.tasteId }.filter { it.value.size > 1 }.keys
        require(duplicateTasteIds.isEmpty()) {
            "Duplicate tasteId is not allowed: $duplicateTasteIds"
        }
    }
}
