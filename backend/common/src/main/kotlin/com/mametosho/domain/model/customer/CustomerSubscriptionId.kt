package com.mametosho.domain.model.customer

/** サブスクリプション契約のID。 */
@JvmInline
value class CustomerSubscriptionId(val value: String) {
    init {
        require(value.isNotBlank()) { "CustomerSubscriptionId must not be blank" }
        require(UUID_REGEX.matches(value)) { "CustomerSubscriptionId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
