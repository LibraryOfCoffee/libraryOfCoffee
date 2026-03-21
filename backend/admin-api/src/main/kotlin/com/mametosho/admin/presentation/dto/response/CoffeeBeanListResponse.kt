package com.mametosho.admin.presentation.dto.response

import com.mametosho.admin.application.query.result.CoffeeBeanListResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆一覧アイテム")
data class CoffeeBeanListResponse(
    @Schema(description = "コーヒー豆ID", example = "00000000-0000-4000-8000-000000000001")
    val id: String,
    @Schema(description = "ショップID", example = "00000000-0000-4000-8000-000000000002")
    val shopId: String,
    @Schema(description = "Shopify商品ID", example = "test-bean-001")
    val shopifyBeanId: String,
    @Schema(description = "豆の名前", example = "エチオピア イルガチェフェ")
    val name: String,
    @Schema(description = "説明", example = "フルーティーな香りが特徴的なコーヒー豆です。")
    val description: String,
    @Schema(description = "産地", example = "エチオピア")
    val origin: String,
    @Schema(description = "農園名", nullable = true, example = "イルガチェフェ農園")
    val farm: String?,
    @Schema(description = "焙煎度", example = "MEDIUM")
    val roastLevel: String,
    @Schema(description = "精製方法", example = "WASHED")
    val processingMethod: String,
    @Schema(description = "スペシャルティコーヒーかどうか", example = "true")
    val isSpecialty: Boolean,
) {
    companion object {
        fun from(result: CoffeeBeanListResult): CoffeeBeanListResponse = CoffeeBeanListResponse(
            id = result.id,
            shopId = result.shopId,
            shopifyBeanId = result.shopifyBeanId,
            name = result.name,
            description = result.description,
            origin = result.origin,
            farm = result.farm,
            roastLevel = result.roastLevel,
            processingMethod = result.processingMethod,
            isSpecialty = result.isSpecialty,
        )
    }
}
