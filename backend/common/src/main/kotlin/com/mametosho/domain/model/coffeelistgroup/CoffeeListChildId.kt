package com.mametosho.domain.model.coffeelistgroup

/** 珈琲リスト明細のID。 */
@JvmInline
value class CoffeeListChildId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeListChildId must not be blank" }
        require(UUID_REGEX.matches(value)) { "CoffeeListChildId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
