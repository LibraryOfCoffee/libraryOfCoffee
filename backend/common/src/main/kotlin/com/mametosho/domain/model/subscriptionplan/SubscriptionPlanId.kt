package com.mametosho.domain.model.subscriptionplan

/** サブスクリプションプランのID。 */
@JvmInline
value class SubscriptionPlanId(val value: String) {
    init {
        require(value.isNotBlank()) { "SubscriptionPlanId must not be blank" }
        require(UUID_REGEX.matches(value)) { "SubscriptionPlanId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
