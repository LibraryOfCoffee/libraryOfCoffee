package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.taste.TasteId

/**
 * コーヒー豆のテイスト評価を表すエンティティ。
 *
 * @property id テイスト評価ID
 * @property tasteId テイスト種別のID
 * @property evaluationValue 評価値。0以上でなければならない
 */
@Suppress("MagicNumber")
data class CoffeeBeanTaste(
    val id: CoffeeBeanTasteId,
    val tasteId: TasteId,
    val evaluationValue: Int,
) {
    init {
        require(evaluationValue in 0..5) { "evaluationValue must be between 0 and 5, but was $evaluationValue" }
    }
}
