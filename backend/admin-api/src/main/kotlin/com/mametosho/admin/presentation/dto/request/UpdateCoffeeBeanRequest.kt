package com.mametosho.admin.presentation.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆更新リクエスト")
data class UpdateCoffeeBeanRequest(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopId: String,

    @Schema(description = "Shopifyの商品ID", example = "test-bean-001", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopifyBeanId: String,

    @Schema(description = "豆の名前", example = "テストコーヒー豆", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,

    @Schema(description = "説明", example = "テスト説明文", requiredMode = Schema.RequiredMode.REQUIRED)
    val description: String,

    @Schema(description = "産地", example = "エチオピア", requiredMode = Schema.RequiredMode.REQUIRED)
    val origin: String,

    @Schema(description = "農園名", example = "テスト農園", nullable = true)
    val farm: String?,

    @Schema(description = "焙煎度", example = "MEDIUM", requiredMode = Schema.RequiredMode.REQUIRED)
    val roastLevel: String,

    @Schema(description = "精製方法", example = "WASHED", requiredMode = Schema.RequiredMode.REQUIRED)
    val processingMethod: String,

    @get:Schema(description = "スペシャルティコーヒーかどうか", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @get:JsonProperty("isSpecialty")
    val isSpecialty: Boolean,

    @Schema(
        description = "公開状態（DRAFT: 下書き / PUBLISHED: 公開）",
        example = "PUBLISHED",
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val publishStatus: String,

    @Schema(description = "テイスト評価一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val tastes: List<TasteRequest>,
) {
    @Schema(description = "テイスト評価リクエスト")
    data class TasteRequest(
        @Schema(description = "テイストID", example = "00000000-0000-4000-8000-000000000041", requiredMode = Schema.RequiredMode.REQUIRED)
        val tasteId: String,

        @Schema(description = "評価値（0-5）", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        val evaluationValue: Int,
    )
}
