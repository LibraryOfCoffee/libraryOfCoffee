package com.mametosho.cs.presentation.dto.response

import com.mametosho.cs.application.result.PagedResult
import com.mametosho.domain.model.shop.Shop
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗一覧レスポンス")
data class ShopListResponse(
    @Schema(description = "アイテム一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val items: List<ShopResponse>,
    @Schema(description = "全件数", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    val totalCount: Long,
    @Schema(description = "現在のページ番号（0始まり）", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    val page: Int,
    @Schema(description = "1ページあたりの件数", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    val size: Int,
) {
    companion object {
        fun from(pagedResult: PagedResult<Shop>): ShopListResponse = ShopListResponse(
            items = pagedResult.items.map { ShopResponse.from(it) },
            totalCount = pagedResult.totalCount,
            page = pagedResult.page,
            size = pagedResult.size,
        )
    }
}
