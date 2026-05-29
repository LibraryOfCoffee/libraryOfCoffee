package com.mametosho.cs.presentation.dto.response

import com.mametosho.cs.application.query.result.ShopListResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗一覧アイテム")
data class ShopListResponse(
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
        fun from(result: ShopListResult): ShopListResponse = ShopListResponse(
            id = result.id,
            name = result.name,
            introduction = result.introduction,
            shopUrl = result.shopUrl,
            prefecture = result.prefecture,
            logoImageUrl = result.logoImageUrl,
        )
    }
}
