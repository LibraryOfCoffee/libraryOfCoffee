package com.mametosho.cs.presentation.dto.response

import com.mametosho.cs.application.query.result.CoffeeBeanSummaryResult
import com.mametosho.cs.application.result.PagedResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "珈琲豆一覧レスポンス")
data class CoffeeBeanListResponse(
    @Schema(description = "アイテム一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val items: List<CoffeeBeanResponse>,
    @Schema(description = "全件数", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    val totalCount: Long,
    @Schema(description = "現在のページ番号（0始まり）", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    val page: Int,
    @Schema(description = "1ページあたりの件数", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    val size: Int,
) {
    companion object {
        fun from(pagedResult: PagedResult<CoffeeBeanSummaryResult>): CoffeeBeanListResponse = CoffeeBeanListResponse(
            items = pagedResult.items.map { CoffeeBeanResponse.from(it) },
            totalCount = pagedResult.totalCount,
            page = pagedResult.page,
            size = pagedResult.size,
        )
    }
}
