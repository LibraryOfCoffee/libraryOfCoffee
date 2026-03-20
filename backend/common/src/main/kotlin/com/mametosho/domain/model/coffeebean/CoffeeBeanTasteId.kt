package com.mametosho.domain.model.coffeebean

/** コーヒー豆のテイスト評価ID。 */
@JvmInline
value class CoffeeBeanTasteId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeBeanTasteId must not be blank" }
        require(UUID_REGEX.matches(value)) { "CoffeeBeanTasteId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
