package com.mametosho.cs.presentation.dto.response

import com.mametosho.cs.application.query.result.CoffeeBeanDetailResult
import com.mametosho.cs.application.query.result.CoffeeBeanImageDetailResult
import com.mametosho.cs.application.query.result.CoffeeBeanTasteDetailResult
import com.mametosho.cs.application.query.result.CoffeeListGroupDetailResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "珈琲リストグループ詳細レスポンス")
data class CoffeeListGroupDetailResponse(
    @Schema(description = "グループID", example = "00000000-0000-4000-8000-000000000051")
    val id: String,

    @Schema(description = "グループの説明", example = "2026年2月のおすすめ珈琲豆")
    val description: String?,

    @Schema(description = "珈琲豆一覧")
    val coffeeBeans: List<CoffeeBeanDetail>,
) {
    @Schema(description = "珈琲豆詳細")
    data class CoffeeBeanDetail(
        @Schema(description = "珈琲豆ID", example = "00000000-0000-4000-8000-000000000071")
        val id: String,

        @Schema(description = "珈琲豆名", example = "エチオピア イルガチェフェ G1")
        val name: String,

        @Schema(description = "説明", example = "花のような華やかなフレーバーと、柑橘系の明るい酸味が特徴。")
        val description: String,

        @Schema(description = "産地", example = "エチオピア")
        val origin: String,

        @Schema(description = "農園", example = "イルガチェフェ コチャレ地区", nullable = true)
        val farm: String?,

        @Schema(description = "焙煎度", example = "light")
        val roastLevel: String,

        @Schema(description = "精製方法", example = "washed")
        val processingMethod: String,

        @Schema(description = "スペシャリティ珈琲かどうか", example = "true")
        val isSpecialty: Boolean,

        @Schema(description = "画像一覧")
        val images: List<CoffeeBeanImageDetail>,

        @Schema(description = "テイスト評価一覧")
        val tastes: List<CoffeeBeanTasteDetail>,
    ) {
        companion object {
            fun from(result: CoffeeBeanDetailResult): CoffeeBeanDetail {
                return CoffeeBeanDetail(
                    id = result.id,
                    name = result.name,
                    description = result.description,
                    origin = result.origin,
                    farm = result.farm,
                    roastLevel = result.roastLevel,
                    processingMethod = result.processingMethod,
                    isSpecialty = result.isSpecialty,
                    images = result.images.map { CoffeeBeanImageDetail.from(it) },
                    tastes = result.tastes.map { CoffeeBeanTasteDetail.from(it) },
                )
            }
        }
    }

    @Schema(description = "珈琲豆画像")
    data class CoffeeBeanImageDetail(
        @Schema(description = "画像ID", example = "00000000-0000-4000-8000-000000000091")
        val id: String,

        @Schema(description = "画像種別", example = "main")
        val type: String,

        @Schema(description = "画像URL", example = "https://placehold.jp/150x150.png")
        val imageUrl: String,
    ) {
        companion object {
            fun from(result: CoffeeBeanImageDetailResult): CoffeeBeanImageDetail {
                return CoffeeBeanImageDetail(
                    id = result.id,
                    type = result.type,
                    imageUrl = result.imageUrl,
                )
            }
        }
    }

    @Schema(description = "珈琲豆テイスト評価")
    data class CoffeeBeanTasteDetail(
        @Schema(description = "テイストID", example = "00000000-0000-4000-8000-000000000041")
        val tasteId: String,

        @Schema(description = "テイスト名", example = "酸味")
        val tasteName: String,

        @Schema(description = "評価値（1〜5）", example = "4")
        val evaluationValue: Int,
    ) {
        companion object {
            fun from(result: CoffeeBeanTasteDetailResult): CoffeeBeanTasteDetail {
                return CoffeeBeanTasteDetail(
                    tasteId = result.tastesId,
                    tasteName = result.name,
                    evaluationValue = result.evaluationValue,
                )
            }
        }
    }

    companion object {
        fun from(result: CoffeeListGroupDetailResult): CoffeeListGroupDetailResponse {
            return CoffeeListGroupDetailResponse(
                id = result.id,
                description = result.description,
                coffeeBeans = result.coffeeBeans.map { CoffeeBeanDetail.from(it) },
            )
        }
    }
}
