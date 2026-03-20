package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.UuidFormat

/** コーヒー豆のテイスト評価ID。 */
@JvmInline
value class CoffeeBeanTasteId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeBeanTasteId must not be blank" }
        require(UuidFormat.isValid(value)) { "CoffeeBeanTasteId must be a valid UUID format" }
    }
}
