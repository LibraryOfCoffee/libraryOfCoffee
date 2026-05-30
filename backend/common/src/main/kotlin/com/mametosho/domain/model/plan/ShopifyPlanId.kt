package com.mametosho.domain.model.plan

/** ShopifyのプランID。 */
@JvmInline
@Suppress("MagicNumber")
value class ShopifyPlanId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopifyPlanId must not be blank" }
        require(value.length <= 255) { "ShopifyPlanId must be at most 255 characters, but was ${value.length}" }
    }
}
