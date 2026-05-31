package com.mametosho.admin.presentation.dto.response

import com.mametosho.admin.application.result.PagedResult
import com.mametosho.domain.model.plan.Plan
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "プラン一覧レスポンス")
data class PlanListResponse(
    @Schema(description = "アイテム一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val items: List<PlanSummaryResponse>,
    @Schema(description = "全件数", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    val totalCount: Long,
    @Schema(description = "現在のページ番号（0始まり）", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    val page: Int,
    @Schema(description = "1ページあたりの件数", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    val size: Int,
) {
    companion object {
        fun from(result: PagedResult<Plan>): PlanListResponse = PlanListResponse(
            items = result.items.map { PlanSummaryResponse.from(it) },
            totalCount = result.totalCount,
            page = result.page,
            size = result.size,
        )
    }
}
