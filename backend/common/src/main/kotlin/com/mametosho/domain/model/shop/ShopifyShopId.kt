package com.mametosho.domain.model.shop

/** ShopifyのショップID。 */
@JvmInline
@Suppress("MagicNumber")
value class ShopifyShopId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopifyShopId must not be blank" }
        require(value.length <= 255) { "ShopifyShopId must be at most 255 characters, but was ${value.length}" }
    }
}
