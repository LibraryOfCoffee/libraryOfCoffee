package com.mametosho.admin.presentation.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.mametosho.admin.application.query.result.CoffeeBeanDetailResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆詳細レスポンス")
data class CoffeeBeanDetailResponse(
    @Schema(description = "コーヒー豆ID", example = "00000000-0000-4000-8000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
    @Schema(description = "ショップID", example = "00000000-0000-4000-8000-000000000002", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopId: String,
    @Schema(description = "店舗名", example = "コーヒーショップ青山", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopName: String,
    @Schema(description = "Shopify商品ID", example = "test-bean-001", requiredMode = Schema.RequiredMode.REQUIRED)
    val shopifyBeanId: String,
    @Schema(description = "豆の名前", example = "エチオピア イルガチェフェ", requiredMode = Schema.RequiredMode.REQUIRED)
    val name: String,
    @Schema(description = "説明", example = "フルーティーな香りが特徴的なコーヒー豆です。", requiredMode = Schema.RequiredMode.REQUIRED)
    val description: String,
    @Schema(description = "産地", example = "エチオピア", requiredMode = Schema.RequiredMode.REQUIRED)
    val origin: String,
    @Schema(description = "農園名", nullable = true, example = "イルガチェフェ農園")
    val farm: String?,
    @Schema(description = "焙煎度", example = "MEDIUM", requiredMode = Schema.RequiredMode.REQUIRED)
    val roastLevel: String,
    @Schema(description = "精製方法", example = "WASHED", requiredMode = Schema.RequiredMode.REQUIRED)
    val processingMethod: String,
    @get:Schema(description = "スペシャルティコーヒーかどうか", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @get:JsonProperty("isSpecialty")
    val isSpecialty: Boolean,
    @Schema(
        description = "公開状態（DRAFT: 下書き / PUBLISHED: 公開）",
        example = "PUBLISHED",
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val publishStatus: String,
    @Schema(description = "画像一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val images: List<ImageDetail>,
    @Schema(description = "テイスト評価一覧", requiredMode = Schema.RequiredMode.REQUIRED)
    val tastes: List<TasteDetail>,
) {
    @Schema(description = "画像詳細")
    data class ImageDetail(
        @Schema(description = "画像ID", example = "00000000-0000-4000-8000-000000000010", requiredMode = Schema.RequiredMode.REQUIRED)
        val id: String,
        @Schema(description = "画像種別", example = "MAIN", requiredMode = Schema.RequiredMode.REQUIRED)
        val type: String,
        @Schema(description = "画像URL", example = "https://example.com/image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
        val imageUrl: String,
    )

    @Schema(description = "テイスト評価詳細")
    data class TasteDetail(
        @Schema(description = "テイスト評価ID", example = "00000000-0000-4000-8000-000000000020", requiredMode = Schema.RequiredMode.REQUIRED)
        val id: String,
        @Schema(description = "テイストマスタID", example = "00000000-0000-4000-8000-000000000041", requiredMode = Schema.RequiredMode.REQUIRED)
        val tasteId: String,
        @Schema(description = "テイスト名", example = "酸味", requiredMode = Schema.RequiredMode.REQUIRED)
        val tasteName: String,
        @Schema(description = "評価値（0-5）", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
        val evaluationValue: Int,
    )

    companion object {
        fun from(result: CoffeeBeanDetailResult): CoffeeBeanDetailResponse = CoffeeBeanDetailResponse(
            id = result.id,
            shopId = result.shopId,
            shopName = result.shopName,
            shopifyBeanId = result.shopifyBeanId,
            name = result.name,
            description = result.description,
            origin = result.origin,
            farm = result.farm,
            roastLevel = result.roastLevel,
            processingMethod = result.processingMethod,
            isSpecialty = result.isSpecialty,
            publishStatus = result.publishStatus,
            images = result.images.map { ImageDetail(id = it.id, type = it.type, imageUrl = it.imageUrl) },
            tastes = result.tastes.map {
                TasteDetail(id = it.id, tasteId = it.tasteId, tasteName = it.tasteName, evaluationValue = it.evaluationValue)
            },
        )
    }
}
