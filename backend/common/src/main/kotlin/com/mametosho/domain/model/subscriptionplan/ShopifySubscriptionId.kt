package com.mametosho.domain.model.subscriptionplan

/** ShopifyのサブスクリプションID。 */
@JvmInline
@Suppress("MagicNumber")
value class ShopifySubscriptionId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopifySubscriptionId must not be blank" }
        require(value.length <= 255) { "ShopifySubscriptionId must be at most 255 characters, but was ${value.length}" }
    }
}
