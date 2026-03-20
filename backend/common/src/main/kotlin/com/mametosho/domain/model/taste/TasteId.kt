package com.mametosho.domain.model.taste

/** テイスト種別のID。 */
@JvmInline
value class TasteId(val value: String) {
    init {
        require(value.isNotBlank()) { "TasteId must not be blank" }
        require(UUID_REGEX.matches(value)) { "TasteId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
