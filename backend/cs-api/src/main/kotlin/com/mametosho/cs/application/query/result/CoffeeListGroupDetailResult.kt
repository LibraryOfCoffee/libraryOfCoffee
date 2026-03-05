package com.mametosho.cs.application.query.result

/**
 * 珈琲リストグループ詳細のクエリ結果。
 */
data class CoffeeListGroupDetailResult(
    val id: String,
    val description: String?,
    val coffeeBeans: List<CoffeeBeanDetailResult>,
)
