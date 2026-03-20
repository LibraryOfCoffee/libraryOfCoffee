package com.mametosho.domain.model.monthlysubscriptiondetail

/** 月次詳細のID。 */
@JvmInline
value class MonthlySubscriptionDetailId(val value: String) {
    init {
        require(value.isNotBlank()) { "MonthlySubscriptionDetailId must not be blank" }
        require(UUID_REGEX.matches(value)) { "MonthlySubscriptionDetailId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
