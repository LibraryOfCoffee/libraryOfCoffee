package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.UuidFormat

/** コーヒー豆のID。 */
@JvmInline
value class CoffeeBeanId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeBeanId must not be blank" }
        require(UuidFormat.isValid(value)) { "CoffeeBeanId must be a valid UUID format" }
    }
}
