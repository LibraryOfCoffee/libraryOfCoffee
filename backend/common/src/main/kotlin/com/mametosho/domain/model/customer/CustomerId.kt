package com.mametosho.domain.model.customer

/** 顧客のID。 */
@JvmInline
value class CustomerId(val value: String) {
    init {
        require(value.isNotBlank()) { "CustomerId must not be blank" }
        require(UUID_REGEX.matches(value)) { "CustomerId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
