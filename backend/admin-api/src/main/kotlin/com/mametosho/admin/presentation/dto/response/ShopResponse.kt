package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shop.Shop
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗登録レスポンス")
data class ShopResponse(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
) {
    companion object {
        fun from(shop: Shop): ShopResponse {
            return ShopResponse(id = shop.id.value)
        }
    }
}
