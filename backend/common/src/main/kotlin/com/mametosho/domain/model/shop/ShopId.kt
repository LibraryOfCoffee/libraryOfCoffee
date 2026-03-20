package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.UuidFormat

/** ショップのID。 */
@JvmInline
value class ShopId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopId must not be blank" }
        require(UuidFormat.isValid(value)) { "ShopId must be a valid UUID format" }
    }
}
