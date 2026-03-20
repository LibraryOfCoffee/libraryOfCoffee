package com.mametosho.domain.model.monthlysubscriptiondetail

import com.mametosho.domain.model.shared.UuidFormat

/** 月次詳細のID。 */
@JvmInline
value class MonthlySubscriptionDetailId(val value: String) {
    init {
        require(value.isNotBlank()) { "MonthlySubscriptionDetailId must not be blank" }
        require(UuidFormat.isValid(value)) { "MonthlySubscriptionDetailId must be a valid UUID format" }
    }
}
