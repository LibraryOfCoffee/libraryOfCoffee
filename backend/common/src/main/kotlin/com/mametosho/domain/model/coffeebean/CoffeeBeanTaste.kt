package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.taste.TasteId

data class CoffeeBeanTaste(
    val id: CoffeeBeanTasteId,
    val tasteId: TasteId,
    val evaluationValue: Int,
) {
    init {
        require(evaluationValue >= 0) { "evaluationValue must be non-negative" }
    }
}
