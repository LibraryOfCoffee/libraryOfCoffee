package com.mametosho.admin.presentation.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.mametosho.admin.application.query.result.CoffeeBeanSummaryResult
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆一覧アイテム")
data class CoffeeBeanSummaryResponse(
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
    val processingMethod: ProcessingMethod,
    @Schema(description = "精製方法の表示名", example = "ウォッシュド", requiredMode = Schema.RequiredMode.REQUIRED)
    val processingMethodName: String,
    @get:Schema(description = "スペシャルティコーヒーかどうか", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @get:JsonProperty("isSpecialty")
    val isSpecialty: Boolean,
    @Schema(
        description = "公開状態（DRAFT: 下書き / PUBLISHED: 公開）",
        example = "PUBLISHED",
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val publishStatus: String,
) {
    companion object {
        fun from(result: CoffeeBeanSummaryResult): CoffeeBeanSummaryResponse {
            val processingMethod = ProcessingMethod.valueOf(result.processingMethod)
            return CoffeeBeanSummaryResponse(
                id = result.id,
                shopId = result.shopId,
                shopName = result.shopName,
                shopifyBeanId = result.shopifyBeanId,
                name = result.name,
                description = result.description,
                origin = result.origin,
                farm = result.farm,
                roastLevel = result.roastLevel,
                processingMethod = processingMethod,
                processingMethodName = processingMethod.label,
                isSpecialty = result.isSpecialty,
                publishStatus = result.publishStatus,
            )
        }
    }
}
