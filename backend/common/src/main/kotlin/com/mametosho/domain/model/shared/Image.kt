package com.mametosho.domain.model.shared

/** 画像を表す値オブジェクト。 */
@JvmInline
@Suppress("MagicNumber")
value class Image(val url: String) {
    companion object {
        const val MAX_FILE_SIZE_BYTES = 1024L * 1024
    }

    init {
        require(url.isNotBlank()) { "Image url must not be blank" }
        require(url.length <= 2048) { "Image url must be at most 2048 characters, but was ${url.length}" }
    }
}
