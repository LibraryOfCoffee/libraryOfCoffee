package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanImage
import com.mametosho.domain.model.coffeebean.CoffeeBeanTaste
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆レスポンス")
data class CoffeeBeanResponse(
    @Schema(description = "コーヒー豆ID", example = "00000000-0000-4000-8000-000000000001")
    val id: String,

    @Schema(description = "店舗ID", example = "00000000-0000-4000-8000-000000000001")
    val shopId: String,

    @Schema(description = "Shopifyの商品ID", example = "test-bean-001")
    val shopifyBeanId: String,

    @Schema(description = "豆の名前", example = "テストコーヒー豆")
    val name: String,

    @Schema(description = "説明", example = "テスト説明文")
    val description: String,

    @Schema(description = "産地", example = "エチオピア")
    val origin: String,

    @Schema(description = "農園名", example = "テスト農園", nullable = true)
    val farm: String?,

    @Schema(description = "焙煎度", example = "MEDIUM")
    val roastLevel: String,

    @Schema(description = "精製方法", example = "WASHED")
    val processingMethod: String,

    @Schema(description = "スペシャルティコーヒーかどうか", example = "true")
    val isSpecialty: Boolean,

    @Schema(description = "画像一覧")
    val images: List<CoffeeBeanImageResponse>,

    @Schema(description = "テイスト評価一覧")
    val tastes: List<CoffeeBeanTasteResponse>,
) {
    @Schema(description = "コーヒー豆画像レスポンス")
    data class CoffeeBeanImageResponse(
        @Schema(description = "画像ID", example = "00000000-0000-4000-8000-000000000011")
        val id: String,

        @Schema(description = "画像種別", example = "MAIN")
        val type: String,

        @Schema(description = "画像URL", example = "https://example.com/bean.png")
        val imageUrl: String,
    ) {
        companion object {
            fun from(image: CoffeeBeanImage): CoffeeBeanImageResponse {
                return CoffeeBeanImageResponse(
                    id = image.id.value,
                    type = image.type.name,
                    imageUrl = image.imageUrl.value,
                )
            }
        }
    }

    @Schema(description = "コーヒー豆テイスト評価レスポンス")
    data class CoffeeBeanTasteResponse(
        @Schema(description = "テイスト評価ID", example = "00000000-0000-4000-8000-000000000021")
        val id: String,

        @Schema(description = "テイストID", example = "00000000-0000-4000-8000-000000000001")
        val tasteId: String,

        @Schema(description = "評価値", example = "3")
        val evaluationValue: Int,
    ) {
        companion object {
            fun from(taste: CoffeeBeanTaste): CoffeeBeanTasteResponse {
                return CoffeeBeanTasteResponse(
                    id = taste.id.value,
                    tasteId = taste.tasteId.value,
                    evaluationValue = taste.evaluationValue,
                )
            }
        }
    }

    companion object {
        fun from(coffeeBean: CoffeeBean): CoffeeBeanResponse {
            return CoffeeBeanResponse(
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
                images = coffeeBean.images.map { CoffeeBeanImageResponse.from(it) },
                tastes = coffeeBean.tastes.map { CoffeeBeanTasteResponse.from(it) },
            )
        }
    }
}
