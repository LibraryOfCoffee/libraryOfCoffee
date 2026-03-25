package com.mametosho.domain.model.administrator

import com.mametosho.domain.model.shared.UuidFormat

/** 管理者のID。 */
@JvmInline
value class AdministratorId(val value: String) {
    init {
        require(value.isNotBlank()) { "AdministratorId must not be blank" }
        require(UuidFormat.isValid(value)) { "AdministratorId must be a valid UUID format" }
    }
}
