package com.mametosho.domain.model.shared

/** 画像のURL。 */
@JvmInline
@Suppress("MagicNumber")
value class ImageUrl(val value: String) {
    init {
        require(value.isNotBlank()) { "ImageUrl must not be blank" }
        require(value.length <= 2048) { "ImageUrl must be at most 2048 characters, but was ${value.length}" }
    }
}
