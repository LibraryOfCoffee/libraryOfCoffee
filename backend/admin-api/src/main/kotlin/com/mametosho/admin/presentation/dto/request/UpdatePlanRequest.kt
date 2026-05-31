package com.mametosho.admin.presentation.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "プラン編集リクエスト")
data class UpdatePlanRequest(
    @Schema(description = "ShopifyのプランID", example = "test-plan-001")
    val shopifyPlanId: String,

    @Schema(description = "プラン表示名", example = "定番")
    val label: String,

    @Schema(description = "1種あたりのグラム数（30 / 60 / 90）", example = "60")
    val gramWeight: Int,

    @Schema(description = "豆の種類数（3 / 4 / 5）", example = "4")
    val beanQuantity: Int,

    @Schema(description = "価格", example = "3800")
    val price: Int,

    @Schema(description = "プラン種別（SUBSCRIPTION / SINGLE）", example = "SUBSCRIPTION")
    val type: String,

    @Schema(description = "おすすめバッジ", example = "true")
    @param:JsonProperty("isRecommended")
    val isRecommended: Boolean,
)
