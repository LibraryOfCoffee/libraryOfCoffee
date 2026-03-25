package com.mametosho.domain.model.administrator

/** メールアドレス。システム内で一意。 */
@JvmInline
@Suppress("MagicNumber")
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "Email must not be blank" }
        require(value.length <= 255) { "Email must be at most 255 characters, but was ${value.length}" }
        require("@" in value) { "Email must contain '@'" }
    }
}
