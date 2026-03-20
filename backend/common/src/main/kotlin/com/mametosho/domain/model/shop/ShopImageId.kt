package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.UuidFormat

/** 店舗画像のID。 */
@JvmInline
value class ShopImageId(val value: String) {
    init {
        require(value.isNotBlank()) { "ShopImageId must not be blank" }
        require(UuidFormat.isValid(value)) { "ShopImageId must be a valid UUID format" }
    }
}
