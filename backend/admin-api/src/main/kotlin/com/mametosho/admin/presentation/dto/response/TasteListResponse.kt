package com.mametosho.admin.presentation.dto.response

import com.mametosho.admin.application.query.result.TasteListResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "テイスト一覧アイテム")
data class TasteListResponse(
    @Schema(description = "テイストID", example = "00000000-0000-4000-8000-000000000041")
    val id: String,
    @Schema(description = "テイスト名", example = "酸味")
    val name: String,
) {
    companion object {
        fun from(result: TasteListResult): TasteListResponse = TasteListResponse(
            id = result.id,
            name = result.name,
        )
    }
}
