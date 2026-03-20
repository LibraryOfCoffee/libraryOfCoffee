package com.mametosho.domain.model.shop

/** 店舗画像のID。 */
@JvmInline
value class ShopImageId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopImageId must not be blank" }
        require(UUID_REGEX.matches(value)) { "ShopImageId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
