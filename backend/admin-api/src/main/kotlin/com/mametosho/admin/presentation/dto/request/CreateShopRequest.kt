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

    @Schema(description = "店舗URL", example = "https://example.com")
    val shopUrl: String,

    @Schema(description = "都道府県", example = "TOKYO")
    val prefecture: String,

    @Schema(description = "公開状態（DRAFT: 下書き / PUBLISHED: 公開）", example = "DRAFT")
    val publishStatus: String,
)
