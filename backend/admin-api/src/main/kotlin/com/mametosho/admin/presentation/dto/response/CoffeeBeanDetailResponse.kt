package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanImage
import com.mametosho.domain.model.coffeebean.CoffeeBeanTaste
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆詳細レスポンス")
data class CoffeeBeanDetailResponse(
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
    @Schema(description = "画像一覧")
    val images: List<ImageDetail>,
    @Schema(description = "テイスト評価一覧")
    val tastes: List<TasteDetail>,
) {
    @Schema(description = "画像詳細")
    data class ImageDetail(
        @Schema(description = "画像ID", example = "00000000-0000-4000-8000-000000000010")
        val id: String,
        @Schema(description = "画像種別", example = "MAIN")
        val type: String,
        @Schema(description = "画像URL", example = "https://example.com/image.jpg")
        val imageUrl: String,
    ) {
        companion object {
            fun from(image: CoffeeBeanImage): ImageDetail = ImageDetail(
                id = image.id.value,
                type = image.type.name,
                imageUrl = image.imageUrl.value,
            )
        }
    }

    @Schema(description = "テイスト評価詳細")
    data class TasteDetail(
        @Schema(description = "テイスト評価ID", example = "00000000-0000-4000-8000-000000000020")
        val id: String,
        @Schema(description = "テイストID", example = "00000000-0000-4000-8000-000000000030")
        val tasteId: String,
        @Schema(description = "評価値（0-5）", example = "4")
        val evaluationValue: Int,
    ) {
        companion object {
            fun from(taste: CoffeeBeanTaste): TasteDetail = TasteDetail(
                id = taste.id.value,
                tasteId = taste.tasteId.value,
                evaluationValue = taste.evaluationValue,
            )
        }
    }

    companion object {
        fun from(coffeeBean: CoffeeBean): CoffeeBeanDetailResponse = CoffeeBeanDetailResponse(
            id = coffeeBean.id.value,
            shopId = coffeeBean.shopId.value,
            shopifyBeanId = coffeeBean.shopifyBeanId.value,
            name = coffeeBean.name,
            description = coffeeBean.description,
            origin = coffeeBean.origin,
            farm = coffeeBean.farm,
            roastLevel = coffeeBean.roastLevel.name,
            processingMethod = coffeeBean.processingMethod.name,
            isSpecialty = coffeeBean.isSpecialty,
            images = coffeeBean.images.map { ImageDetail.from(it) },
            tastes = coffeeBean.tastes.map { TasteDetail.from(it) },
        )
    }
}
