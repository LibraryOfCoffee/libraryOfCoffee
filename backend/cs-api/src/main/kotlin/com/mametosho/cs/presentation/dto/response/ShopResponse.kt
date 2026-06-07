package com.mametosho.cs.presentation.dto.response

import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopImageType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗一覧アイテム")
data class ShopResponse(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000031")
    val id: String,
    @Schema(description = "店舗名", example = "珈琲工房 まめとしょ")
    val name: String,
    @Schema(description = "店舗紹介文", example = "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。")
    val introduction: String,
    @Schema(description = "店舗URL", example = "https://mametosho.example.com")
    val shopUrl: String,
    @Schema(description = "都道府県", example = "TOKYO")
    val prefecture: String,
    @Schema(description = "ロゴ画像URL", example = "https://placehold.jp/100x100.png")
    val logoImageUrl: String,
) {
    companion object {
        fun from(shop: Shop): ShopResponse = ShopResponse(
            id = shop.id.value,
            name = shop.name,
            introduction = shop.introduction ?: "",
            shopUrl = shop.shopUrl,
            prefecture = shop.prefecture.name,
            logoImageUrl = shop.images.first { it.type == ShopImageType.LOGO }.image.value,
        )
    }
}
