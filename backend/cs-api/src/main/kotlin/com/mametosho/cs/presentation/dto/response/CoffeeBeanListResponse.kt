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
) {
    companion object {
        fun from(result: CoffeeBeanListResult): CoffeeBeanListResponse = CoffeeBeanListResponse(
            id = result.id,
            name = result.name,
            origin = result.origin,
            roastLevel = result.roastLevel,
            processingMethod = result.processingMethod,
            isSpecialty = result.isSpecialty,
        )
    }
}
