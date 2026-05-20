package com.mametosho.admin.presentation.dto.response

import com.mametosho.admin.application.query.result.ShopListResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗一覧アイテム")
data class ShopListResponse(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000001")
    val id: String,
    @Schema(description = "ShopifyショップID", example = "test-shop-001")
    val shopifyShopId: String,
    @Schema(description = "店舗名", example = "テスト珈琲店")
    val name: String,
    @Schema(description = "紹介文", nullable = true, example = "こだわりの珈琲をお届けします。")
    val introduction: String?,
    @Schema(description = "こだわり", nullable = true, example = "厳選された豆のみを使用しています。")
    val particular: String?,
    @Schema(description = "店舗URL", example = "https://example.com")
    val shopUrl: String,
    @Schema(description = "都道府県", example = "TOKYO")
    val prefecture: String,
) {
    companion object {
        fun from(result: ShopListResult): ShopListResponse = ShopListResponse(
            id = result.id,
            shopifyShopId = result.shopifyShopId,
            name = result.name,
            introduction = result.introduction,
            particular = result.particular,
            shopUrl = result.shopUrl,
            prefecture = result.prefecture,
        )
    }
}
