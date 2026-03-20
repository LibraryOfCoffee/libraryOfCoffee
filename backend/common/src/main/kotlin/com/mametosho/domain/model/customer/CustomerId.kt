package com.mametosho.domain.model.customer

import com.mametosho.domain.model.shared.UuidFormat

/** 顧客のID。 */
@JvmInline
value class CustomerId(val value: String) {
    init {
        require(value.isNotBlank()) { "CustomerId must not be blank" }
        require(UuidFormat.isValid(value)) { "CustomerId must be a valid UUID format" }
    }
}
