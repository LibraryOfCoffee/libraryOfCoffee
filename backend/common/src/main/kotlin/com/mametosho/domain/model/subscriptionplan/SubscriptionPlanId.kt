package com.mametosho.domain.model.subscriptionplan

import com.mametosho.domain.model.shared.UuidFormat

/** サブスクリプションプランのID。 */
@JvmInline
value class SubscriptionPlanId(val value: String) {
    init {
        require(value.isNotBlank()) { "SubscriptionPlanId must not be blank" }
        require(UuidFormat.isValid(value)) { "SubscriptionPlanId must be a valid UUID format" }
    }
}
