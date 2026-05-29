package com.mametosho.admin.presentation.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆登録リクエスト")
data class CreateCoffeeBeanRequest(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000031")
    val shopId: String,

    @Schema(description = "Shopifyの商品ID", example = "gid://shopify/Product/999999")
    val shopifyBeanId: String,

    @Schema(description = "豆の名前", example = "テストコーヒー豆")
    val name: String,

    @Schema(description = "説明", example = "テスト説明文")
    val description: String,

    @Schema(description = "産地", example = "エチオピア")
    val origin: String,

    @Schema(description = "農園名", example = "テスト農園", nullable = true)
    val farm: String?,

    @Schema(description = "焙煎度", example = "MEDIUM")
    val roastLevel: String,

    @Schema(description = "精製方法", example = "WASHED")
    val processingMethod: String,

    @Schema(description = "スペシャルティコーヒーかどうか", example = "true")
    val isSpecialty: Boolean,

    @Schema(description = "テイスト評価一覧")
    val tastes: List<TasteRequest>,
) {
    @Schema(description = "テイスト評価リクエスト")
    data class TasteRequest(
        @Schema(description = "テイスト名", example = "酸味")
        val tasteName: String,

        @Schema(description = "評価値（0-5）", example = "3")
        val evaluationValue: Int,
    )
}
