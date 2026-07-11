package com.mametosho.cs.presentation.dto.response

import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopImage
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗詳細レスポンス")
data class ShopDetailResponse(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000031", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
    @Schema(description = "店舗名", example = "珈琲工房 まめとしょ", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,
    @Schema(
        description = "店舗紹介文",
        example = "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val introduction: String,
    @Schema(description = "こだわり", example = "厳選された豆のみを使用しています。", requiredMode = Schema.RequiredMode.REQUIRED)
    val particular: String,
    @Schema(description = "店舗URL", example = "https://mametosho.example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopUrl: String,
    @Schema(description = "都道府県", example = "TOKYO", requiredMode = Schema.RequiredMode.REQUIRED)
    val prefecture: String,
    @Schema(description = "画像一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val images: List<ImageDetail>,
) {
    @Schema(description = "画像詳細")
    data class ImageDetail(
        @Schema(description = "画像ID", example = "00000000-0000-4000-8000-000000000010", requiredMode = Schema.RequiredMode.REQUIRED)
        val id: String,
        @Schema(description = "画像種別（LOGO: ロゴ / MAIN: メイン）", example = "LOGO", requiredMode = Schema.RequiredMode.REQUIRED)
        val type: String,
        @Schema(description = "画像URL", example = "https://placehold.jp/100x100.png", requiredMode = Schema.RequiredMode.REQUIRED)
        val imageUrl: String,
    ) {
        companion object {
            fun from(image: ShopImage): ImageDetail = ImageDetail(
                id = image.id.value,
                type = image.type.name,
                imageUrl = image.image.url,
            )
        }
    }

    companion object {
        fun from(shop: Shop): ShopDetailResponse = ShopDetailResponse(
            id = shop.id.value,
            name = shop.name,
            introduction = shop.introduction ?: "",
            particular = shop.particular ?: "",
            shopUrl = shop.shopUrl,
            prefecture = shop.prefecture.name,
            images = shop.images.map { ImageDetail.from(it) },
        )
    }
}
