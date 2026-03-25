package com.mametosho.domain.model.coffeebean

/** Shopifyのコーヒー豆商品ID。 */
@JvmInline
@Suppress("MagicNumber")
value class ShopifyBeanId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopifyBeanId must not be blank" }
        require(value.length <= 255) { "ShopifyBeanId must be at most 255 characters, but was ${value.length}" }
    }
}
