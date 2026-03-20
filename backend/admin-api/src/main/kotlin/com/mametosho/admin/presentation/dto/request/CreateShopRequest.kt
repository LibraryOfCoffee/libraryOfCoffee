package com.mametosho.admin.presentation.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗登録リクエスト")
data class CreateShopRequest(
    @Schema(description = "ShopifyのショップID", example = "test-shop-001")
    val shopifyShopId: String,

    @Schema(description = "店舗名", example = "テスト店舗")
    val name: String,

    @Schema(description = "店舗紹介", example = "テスト紹介文", nullable = true)
    val introduction: String?,

    @Schema(description = "こだわり", example = "テストこだわり", nullable = true)
    val particular: String?,

    @Schema(description = "店舗画像一覧")
    val images: List<ImageRequest>,
) {
    @Schema(description = "店舗画像リクエスト")
    data class ImageRequest(
        @Schema(description = "画像種別", example = "MAIN")
        val type: String,

        @Schema(description = "画像URL", example = "https://example.com/image.png")
        val imageUrl: String,
    )
}
