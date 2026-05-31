package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.plan.Plan
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "プラン編集レスポンス")
data class PlanResponse(
    @Schema(description = "プランID", example = "00000000-0000-4000-8000-000000000024", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
) {
    companion object {
        fun from(plan: Plan): PlanResponse {
            return PlanResponse(id = plan.id.value)
        }
    }
}
