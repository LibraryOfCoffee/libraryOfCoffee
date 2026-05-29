package com.mametosho.cs.presentation.dto.response

import com.mametosho.cs.application.query.result.PlanListResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "プラン一覧アイテム")
data class PlanListResponse(
    @Schema(description = "プランID", example = "00000000-0000-4000-8000-000000000024")
    val id: String,
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
    val isRecommended: Boolean,
) {
    companion object {
        fun from(result: PlanListResult): PlanListResponse = PlanListResponse(
            id = result.id,
            label = result.label,
            gramWeight = result.gramWeight,
            beanQuantity = result.beanQuantity,
            price = result.price,
            type = result.type,
            isRecommended = result.isRecommended,
        )
    }
}
