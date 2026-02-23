package com.mametosho.cs.application.query.result

/**
 * 珈琲豆テイスト評価のクエリ結果。
 */
data class CoffeeBeanTasteDetailResult(
    val id: String,
    val tastesId: String,
    val evaluationValue: Int,
    val name: String,
)
