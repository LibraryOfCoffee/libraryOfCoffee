package com.mametosho.admin.application.query.result

data class CoffeeBeanDetailResult(
    val id: String,
    val shopId: String,
    val shopName: String,
    val shopifyBeanId: String,
    val name: String,
    val description: String,
    val origin: String,
    val farm: String?,
    val roastLevel: String,
    val processingMethod: String,
    val isSpecialty: Boolean,
    val publishStatus: String,
    val images: List<ImageResult>,
    val tastes: List<TasteResult>,
) {
    data class ImageResult(
        val id: String,
        val type: String,
        val imageUrl: String,
    )

    data class TasteResult(
        val id: String,
        val tasteId: String,
        val tasteName: String,
        val evaluationValue: Int,
    )
}
