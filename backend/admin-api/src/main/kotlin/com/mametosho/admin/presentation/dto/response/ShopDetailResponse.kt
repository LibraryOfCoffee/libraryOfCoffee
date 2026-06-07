package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopImage
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗詳細レスポンス")
data class ShopDetailResponse(
    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
    @Schema(description = "ShopifyショップID", example = "test-shop-001", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopifyShopId: String,
    @Schema(description = "店舗名", example = "テスト珈琲店", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,
    @Schema(description = "店舗紹介", nullable = true, example = "こだわりの珈琲をお届けします。")
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
    @Schema(description = "画像一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val images: List<ImageDetail>,
) {
    @Schema(description = "画像詳細")
    data class ImageDetail(
        @Schema(description = "画像ID", example = "00000000-0000-4000-8000-000000000010", requiredMode = Schema.RequiredMode.REQUIRED)
        val id: String,
        @Schema(description = "画像種別", example = "MAIN", requiredMode = Schema.RequiredMode.REQUIRED)
        val type: String,
        @Schema(description = "画像URL", example = "https://example.com/shop-image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
        val imageUrl: String,
    ) {
        companion object {
            fun from(image: ShopImage): ImageDetail = ImageDetail(
                id = image.id.value,
                type = image.type.name,
                imageUrl = image.image.value,
            )
        }
    }

    companion object {
        fun from(shop: Shop): ShopDetailResponse = ShopDetailResponse(
            id = shop.id.value,
            shopifyShopId = shop.shopifyShopId.value,
            name = shop.name,
            introduction = shop.introduction,
            particular = shop.particular,
            shopUrl = shop.shopUrl,
            prefecture = shop.prefecture.name,
            participationStatus = shop.participationStatus.name,
            images = shop.images.map { ImageDetail.from(it) },
        )
    }
}
