package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.coffeebean.CoffeeBean
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "コーヒー豆登録レスポンス")
data class CoffeeBeanResponse(
    @Schema(description = "コーヒー豆ID", example = "00000000-0000-4000-8000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    val id: String,
) {
    companion object {
        fun from(coffeeBean: CoffeeBean): CoffeeBeanResponse {
            return CoffeeBeanResponse(id = coffeeBean.id.value)
        }
    }
}
