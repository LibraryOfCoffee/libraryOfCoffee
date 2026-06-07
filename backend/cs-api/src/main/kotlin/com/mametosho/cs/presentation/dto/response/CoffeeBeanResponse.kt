package com.mametosho.cs.presentation.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.mametosho.cs.application.query.result.CoffeeBeanSummaryResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "珈琲豆一覧アイテム")
data class CoffeeBeanResponse(
    @Schema(description = "珈琲豆ID", example = "00000000-0000-4000-8000-000000000071", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
    @Schema(
        description = "ShopifyのID（Shopify遷移時に使用する）",
        example = "gid://shopify/Product/400001",
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val shopifyBeanId: String,
    @Schema(description = "珈琲豆名", example = "エチオピア イルガチェフェ G1", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,
    @Schema(description = "産地", example = "エチオピア", requiredMode = Schema.RequiredMode.REQUIRED)
    val origin: String,
    @Schema(description = "焙煎度", example = "LIGHT", requiredMode = Schema.RequiredMode.REQUIRED)
    val roastLevel: String,
    @Schema(description = "精製方法", example = "WASHED", requiredMode = Schema.RequiredMode.REQUIRED)
    val processingMethod: String,
    @get:Schema(description = "スペシャルティ珈琲かどうか", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @get:JsonProperty("isSpecialty")
    val isSpecialty: Boolean,
    @Schema(description = "説明文", example = "フルーティーな香りと明るい酸味が特徴の豆です。", requiredMode = Schema.RequiredMode.REQUIRED)
    val description: String,
    @Schema(description = "メイン画像URL", example = "https://example.com/images/coffee.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    val imageUrl: String,
    @Schema(description = "ロースター名", example = "山田珈琲焙煎所", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopName: String,
    @Schema(description = "ロースターの都道府県", example = "TOKYO", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopPrefecture: String,
    @Schema(description = "ロースターのURL", example = "https://mametosho.example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopUrl: String,
    @Schema(description = "テイストプロファイル一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val tasteProfiles: List<TasteProfileResponse>,
) {
    @Schema(description = "テイストプロファイル")
    data class TasteProfileResponse(
        @Schema(description = "テイスト名", example = "酸味", requiredMode = Schema.RequiredMode.REQUIRED)
        val name: String,
        @Schema(description = "評価値", example = "60", requiredMode = Schema.RequiredMode.REQUIRED)
        val value: Int,
    )

    companion object {
        fun from(result: CoffeeBeanSummaryResult): CoffeeBeanResponse = CoffeeBeanResponse(
            id = result.id,
            shopifyBeanId = result.shopifyBeanId,
            name = result.name,
            origin = result.origin,
            roastLevel = result.roastLevel,
            processingMethod = result.processingMethod,
            isSpecialty = result.isSpecialty,
            description = result.description,
            imageUrl = result.imageUrl,
            shopName = result.shopName,
            shopPrefecture = result.shopPrefecture,
            shopUrl = result.shopUrl,
            tasteProfiles = result.tasteProfiles.map { taste ->
                TasteProfileResponse(name = taste.name, value = taste.value)
            },
        )
    }
}
