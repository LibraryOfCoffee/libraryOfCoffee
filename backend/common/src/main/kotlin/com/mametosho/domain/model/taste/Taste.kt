package com.mametosho.domain.model.taste

/**
 * テイスト種別を表す集約ルート。
 *
 * 「酸味」「苦味」「コク」など、珈琲豆のテイスト評価の種別を管理する。
 *
 * @property id テイストID
 * @property name テイスト名。システム内で一意
 */
data class Taste(
    val id: TasteId,
    val name: String,
) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
    }
}
