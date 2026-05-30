package com.mametosho.cs.application.query.result

data class CoffeeBeanSummaryResult(
    val id: String,
    val name: String,
    val origin: String,
    val roastLevel: String,
    val processingMethod: String,
    val isSpecialty: Boolean,
    val description: String,
    val imageUrl: String,
    val shopName: String,
    val shopPrefecture: String,
    val shopUrl: String,
    val tasteProfiles: List<TasteProfileResult>,
) {
    data class TasteProfileResult(
        val name: String,
        val value: Int,
    )
}
