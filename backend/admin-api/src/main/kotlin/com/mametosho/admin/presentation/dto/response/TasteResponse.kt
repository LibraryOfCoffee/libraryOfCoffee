package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.taste.Taste
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "テイスト一覧アイテム")
data class TasteResponse(
    @Schema(description = "テイストID", example = "00000000-0000-4000-8000-000000000041", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
    @Schema(description = "テイスト名", example = "酸味", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,
) {
    companion object {
        fun from(taste: Taste): TasteResponse = TasteResponse(
            id = taste.id.value,
            name = taste.name,
        )
    }
}
