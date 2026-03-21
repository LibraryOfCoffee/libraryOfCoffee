package com.mametosho.admin.presentation.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆更新リクエスト")
data class UpdateCoffeeBeanRequest(
    @Schema(description = "Shopifyの商品ID", example = "test-bean-001")
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

    @Schema(description = "画像一覧")
    val images: List<ImageRequest>,

    @Schema(description = "テイスト評価一覧")
    val tastes: List<TasteRequest>,
) {
    @Schema(description = "コーヒー豆画像リクエスト")
    data class ImageRequest(
        @Schema(description = "画像種別", example = "MAIN")
        val type: String,

        @Schema(description = "画像URL", example = "https://example.com/bean.png")
        val imageUrl: String,
    )

    @Schema(description = "テイスト評価リクエスト")
    data class TasteRequest(
        @Schema(description = "テイストID", example = "00000000-0000-4000-8000-000000000041")
        val tasteId: String,

        @Schema(description = "評価値", example = "3")
        val evaluationValue: Int,
    )
}
