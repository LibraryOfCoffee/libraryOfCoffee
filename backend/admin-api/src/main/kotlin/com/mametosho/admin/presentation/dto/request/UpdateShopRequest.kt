package com.mametosho.admin.presentation.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "店舗編集リクエスト")
data class UpdateShopRequest(
    @Schema(description = "ShopifyのショップID", example = "test-shop-001", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopifyShopId: String,

    @Schema(description = "店舗名", example = "テスト店舗", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,

    @Schema(description = "店舗紹介", example = "テスト紹介文", nullable = true)
    val introduction: String?,

    @Schema(description = "こだわり", example = "テストこだわり", nullable = true)
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

    @Schema(description = "アップロード画像のタイプ一覧（imagesと同じ順序）", example = "[\"MAIN\"]")
    val imageTypes: List<String> = emptyList(),

    @Schema(description = "保持する既存画像のID一覧（含まれない既存画像は削除される）")
    val keepImageIds: List<String> = emptyList(),
)
