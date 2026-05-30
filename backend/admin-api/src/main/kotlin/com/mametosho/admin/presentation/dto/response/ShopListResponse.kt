package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shop.Shop
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
        fun from(shop: Shop): ShopListResponse = ShopListResponse(
            id = shop.id.value,
            shopifyShopId = shop.shopifyShopId.value,
            name = shop.name,
            introduction = shop.introduction,
            particular = shop.particular,
            shopUrl = shop.shopUrl,
            prefecture = shop.prefecture.name,
        )
    }
}
