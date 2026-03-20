package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.UuidFormat

/** コーヒー豆の画像ID。 */
@JvmInline
value class CoffeeBeanImageId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeBeanImageId must not be blank" }
        require(UuidFormat.isValid(value)) { "CoffeeBeanImageId must be a valid UUID format" }
    }
}
