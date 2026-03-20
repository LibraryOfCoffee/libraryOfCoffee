package com.mametosho.domain.model.coffeelistgroup

import com.mametosho.domain.model.shared.UuidFormat

/** 珈琲リスト明細のID。 */
@JvmInline
value class CoffeeListChildId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeListChildId must not be blank" }
        require(UuidFormat.isValid(value)) { "CoffeeListChildId must be a valid UUID format" }
    }
}
