package com.mametosho.cs.application.query.result

/**
 * 珈琲豆詳細のクエリ結果。
 */
data class CoffeeBeanDetailResult(
    val id: String,
    val name: String,
    val description: String,
    val origin: String,
    val farm: String?,
    val roastLevel: String,
    val processingMethod: String,
    val isSpecialty: Boolean,
    val images: List<CoffeeBeanImageDetailResult>,
    val tastes: List<CoffeeBeanTasteDetailResult>,
)
