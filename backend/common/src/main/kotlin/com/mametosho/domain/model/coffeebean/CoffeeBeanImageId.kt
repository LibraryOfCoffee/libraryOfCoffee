package com.mametosho.domain.model.coffeebean

/** コーヒー豆の画像ID。 */
@JvmInline
value class CoffeeBeanImageId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeBeanImageId must not be blank" }
        require(UUID_REGEX.matches(value)) { "CoffeeBeanImageId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
