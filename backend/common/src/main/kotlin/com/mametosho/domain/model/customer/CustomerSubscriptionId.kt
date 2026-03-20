package com.mametosho.domain.model.customer

import com.mametosho.domain.model.shared.UuidFormat

/** サブスクリプション契約のID。 */
@JvmInline
value class CustomerSubscriptionId(val value: String) {
    init {
        require(value.isNotBlank()) { "CustomerSubscriptionId must not be blank" }
        require(UuidFormat.isValid(value)) { "CustomerSubscriptionId must be a valid UUID format" }
    }
}
