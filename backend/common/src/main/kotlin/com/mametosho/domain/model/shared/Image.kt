package com.mametosho.domain.model.shared

/** 画像を表す値オブジェクト。 */
@JvmInline
@Suppress("MagicNumber")
value class Image(val value: String) {
    companion object {
        const val MAX_FILE_SIZE_BYTES = 1L * 1024 * 1024
    }

    init {
        require(value.isNotBlank()) { "Image must not be blank" }
        require(value.length <= 2048) { "Image must be at most 2048 characters, but was ${value.length}" }
    }
}
