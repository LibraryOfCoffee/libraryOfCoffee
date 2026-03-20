package com.mametosho.domain.model.taste

import com.mametosho.domain.model.shared.UuidFormat

/** テイスト種別のID。 */
@JvmInline
value class TasteId(val value: String) {
    init {
        require(value.isNotBlank()) { "TasteId must not be blank" }
        require(UuidFormat.isValid(value)) { "TasteId must be a valid UUID format" }
    }
}
