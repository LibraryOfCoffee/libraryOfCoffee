package com.mametosho.domain.model.shared

/** UUID形式の正規表現。各ID値オブジェクトのバリデーションで共有する。 */
object UuidFormat {
    private val REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)

    fun isValid(value: String): Boolean = REGEX.matches(value)
}
