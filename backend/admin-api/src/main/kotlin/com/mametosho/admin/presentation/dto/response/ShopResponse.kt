package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shop.Shop
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗レスポンス")
data class ShopResponse(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000001")
    val id: String,

    @Schema(description = "ShopifyのショップID", example = "test-shop-001")
    val shopifyShopId: String,

    @Schema(description = "店舗名", example = "テスト店舗")
    val name: String,

    @Schema(description = "店舗紹介", example = "テスト紹介文", nullable = true)
    val introduction: String?,

    @Schema(description = "こだわり", example = "テストこだわり", nullable = true)
    val particular: String?,

    @Schema(description = "店舗画像一覧")
    val images: List<ShopImageResponse>,
) {
    @Schema(description = "店舗画像レスポンス")
    data class ShopImageResponse(
        @Schema(description = "画像ID", example = "00000000-0000-4000-8000-000000000011")
        val id: String,

        @Schema(description = "画像種別", example = "MAIN")
        val type: String,

        @Schema(description = "画像URL", example = "https://example.com/image.png")
        val imageUrl: String,
    ) {
        companion object {
            fun from(image: com.mametosho.domain.model.shop.ShopImage): ShopImageResponse {
                return ShopImageResponse(
                    id = image.id.value,
                    type = image.type.name,
                    imageUrl = image.imageUrl.value,
                )
            }
        }
    }

    companion object {
        fun from(shop: Shop): ShopResponse {
            return ShopResponse(
                id = shop.id.value,
                shopifyShopId = shop.shopifyShopId.value,
                name = shop.name,
                introduction = shop.introduction,
                particular = shop.particular,
                images = shop.images.map { ShopImageResponse.from(it) },
            )
        }
    }
}
