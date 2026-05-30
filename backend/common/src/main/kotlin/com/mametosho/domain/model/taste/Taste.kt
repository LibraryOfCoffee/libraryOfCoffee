package com.mametosho.domain.model.taste

/**
 * テイスト種別を表す集約ルート。
 *
 * 「酸味」「苦味」「コク」など、珈琲豆のテイスト評価の種別を管理する。
 *
 * @property name システム内で一意
 */
@Suppress("MagicNumber")
data class Taste(
    val id: TasteId,
    val name: String,
) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
        require(name.length <= 255) { "name must be at most 255 characters, but was ${name.length}" }
    }
}
