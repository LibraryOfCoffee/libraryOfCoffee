package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.taste.TasteId

/**
 * コーヒー豆のテイスト評価を表すエンティティ。
 *
 * @property id テイスト評価ID
 * @property tasteId テイスト種別のID
 * @property evaluationValue 評価値。0以上でなければならない
 */
data class CoffeeBeanTaste(
    val id: CoffeeBeanTasteId,
    val tasteId: TasteId,
    val evaluationValue: Int,
) {
    init {
        require(evaluationValue >= 0) { "evaluationValue must be non-negative" }
    }
}
