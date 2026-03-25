package com.mametosho.domain.model.coffeelistgroup

import com.mametosho.domain.model.shared.UuidFormat

/** 珈琲リストグループのID。 */
@JvmInline
value class CoffeeListGroupId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeListGroupId must not be blank" }
        require(UuidFormat.isValid(value)) { "CoffeeListGroupId must be a valid UUID format" }
    }
}
