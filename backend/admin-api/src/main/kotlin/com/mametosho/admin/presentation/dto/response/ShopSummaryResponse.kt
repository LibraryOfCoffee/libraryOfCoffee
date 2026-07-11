package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shop.Shop
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗一覧アイテム")
data class ShopSummaryResponse(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
    @Schema(description = "ShopifyショップID", example = "test-shop-001", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopifyShopId: String,
    @Schema(description = "店舗名", example = "テスト珈琲店", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,
    @Schema(description = "紹介文", nullable = true, example = "こだわりの珈琲をお届けします。")
    val introduction: String?,
    @Schema(description = "こだわり", nullable = true, example = "厳選された豆のみを使用しています。")
    val particular: String?,
    @Schema(description = "店舗URL", example = "https://example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopUrl: String,
    @Schema(description = "都道府県", example = "TOKYO", requiredMode = Schema.RequiredMode.REQUIRED)
    val prefecture: String,
    @Schema(
        description = "参画ステータス（BEFORE_PARTICIPATION: 参画前 / PARTICIPATING: 参画中 / DROPPED: 参画落ち）",
        example = "PARTICIPATING",
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val participationStatus: String,
) {
    companion object {
        fun from(shop: Shop): ShopSummaryResponse = ShopSummaryResponse(
            id = shop.id.value,
            shopifyShopId = shop.shopifyShopId.value,
            name = shop.name,
            introduction = shop.introduction,
            particular = shop.particular,
            shopUrl = shop.shopUrl,
            prefecture = shop.prefecture.name,
            participationStatus = shop.participationStatus.name,
        )
    }
}
