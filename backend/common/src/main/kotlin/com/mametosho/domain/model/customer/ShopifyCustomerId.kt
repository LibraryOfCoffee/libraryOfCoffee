package com.mametosho.domain.model.customer

/** Shopifyの顧客ID。システム内で一意。 */
@JvmInline
@Suppress("MagicNumber")
value class ShopifyCustomerId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopifyCustomerId must not be blank" }
        require(value.length <= 255) { "ShopifyCustomerId must be at most 255 characters, but was ${value.length}" }
    }
}
