package com.mametosho.domain.model.shop

/** ショップのID。 */
@JvmInline
value class ShopId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopId must not be blank" }
        require(UUID_REGEX.matches(value)) { "ShopId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
