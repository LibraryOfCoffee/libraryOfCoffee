package com.mametosho.cs.presentation.dto.response

import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "珈琲豆一覧アイテム")
data class CoffeeBeanListResponse(
    @Schema(description = "珈琲豆ID", example = "00000000-0000-4000-8000-000000000071")
    val id: String,
    @Schema(description = "珈琲豆名", example = "エチオピア イルガチェフェ G1")
    val name: String,
    @Schema(description = "産地", example = "エチオピア")
    val origin: String,
    @Schema(description = "焙煎度", example = "LIGHT")
    val roastLevel: String,
    @Schema(description = "精製方法", example = "WASHED")
    val processingMethod: String,
    @Schema(description = "スペシャルティ珈琲かどうか", example = "true")
    val isSpecialty: Boolean,
    @Schema(description = "説明文", example = "フルーティーな香りと明るい酸味が特徴の豆です。")
    val description: String,
    @Schema(description = "メイン画像URL", example = "https://example.com/images/coffee.jpg")
    val imageUrl: String,
    @Schema(description = "ロースター名", example = "山田珈琲焙煎所")
    val shopName: String,
    @Schema(description = "ロースターの都道府県", example = "TOKYO")
    val shopPrefecture: String,
    @Schema(description = "テイストプロファイル一覧")
    val tasteProfiles: List<TasteProfileResponse>,
) {
    @Schema(description = "テイストプロファイル")
    data class TasteProfileResponse(
        @Schema(description = "テイスト名", example = "酸味")
        val name: String,
        @Schema(description = "評価値", example = "60")
        val value: Int,
    )

    companion object {
        fun from(result: CoffeeBeanListResult): CoffeeBeanListResponse = CoffeeBeanListResponse(
            id = result.id,
            name = result.name,
            origin = result.origin,
            roastLevel = result.roastLevel,
            processingMethod = result.processingMethod,
            isSpecialty = result.isSpecialty,
            description = result.description,
            imageUrl = result.imageUrl,
            shopName = result.shopName,
            shopPrefecture = result.shopPrefecture,
            tasteProfiles = result.tasteProfiles.map { taste ->
                TasteProfileResponse(name = taste.name, value = taste.value)
            },
        )
    }
}
