package com.mametosho.domain.model.coffeelistgroup

/** 珈琲リストグループのID。 */
@JvmInline
value class CoffeeListGroupId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeListGroupId must not be blank" }
        require(UUID_REGEX.matches(value)) { "CoffeeListGroupId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
