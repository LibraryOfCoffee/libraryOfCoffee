package com.mametosho.admin.presentation.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.mametosho.domain.model.plan.Plan
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "プラン詳細レスポンス")
data class PlanDetailResponse(
    @Schema(description = "プランID", example = "00000000-0000-4000-8000-000000000024")
    val id: String,
    @Schema(description = "ShopifyのプランID", example = "test-plan-001")
    val shopifyPlanId: String,
    @Schema(description = "プラン表示名", example = "定番")
    val label: String,
    @Schema(description = "1種あたりのグラム数", example = "60")
    val gramWeight: Int,
    @Schema(description = "豆の種類数", example = "4")
    val beanQuantity: Int,
    @Schema(description = "価格", example = "3800")
    val price: Int,
    @Schema(description = "プラン種別（SUBSCRIPTION / SINGLE）", example = "SUBSCRIPTION")
    val type: String,
    @Schema(description = "おすすめバッジ", example = "true")
    @get:JsonProperty("isRecommended")
    val isRecommended: Boolean,
) {
    companion object {
        fun from(plan: Plan): PlanDetailResponse = PlanDetailResponse(
            id = plan.id.value,
            shopifyPlanId = plan.shopifyPlanId.value,
            label = plan.label,
            gramWeight = plan.gramWeight,
            beanQuantity = plan.beanQuantity,
            price = plan.price,
            type = plan.type.name,
            isRecommended = plan.isRecommended,
        )
    }
}
