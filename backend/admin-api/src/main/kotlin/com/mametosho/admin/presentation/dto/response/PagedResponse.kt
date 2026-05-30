package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.PagedResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "ページネーション付きレスポンス")
data class PagedResponse<T>(
    @Schema(description = "アイテム一覧")
    val items: List<T>,
    @Schema(description = "全件数", example = "42")
    val totalCount: Long,
    @Schema(description = "現在のページ番号（0始まり）", example = "0")
    val page: Int,
    @Schema(description = "1ページあたりの件数", example = "20")
    val size: Int,
) {
    companion object {
        fun <T, R> from(
            pagedResult: PagedResult<T>,
            mapper: (T) -> R,
        ): PagedResponse<R> = PagedResponse(
            items = pagedResult.items.map(mapper),
            totalCount = pagedResult.totalCount,
            page = pagedResult.page,
            size = pagedResult.size,
        )
    }
}
