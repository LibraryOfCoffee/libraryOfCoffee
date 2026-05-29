package com.mametosho.domain.model.subscriptionplan

import com.mametosho.domain.model.shared.UuidFormat

/** プランのID。 */
@JvmInline
value class PlanId(val value: String) {
    init {
        require(value.isNotBlank()) { "PlanId must not be blank" }
        require(UuidFormat.isValid(value)) { "PlanId must be a valid UUID format" }
    }
}
