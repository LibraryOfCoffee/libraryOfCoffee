package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.coffeebean.ProcessingMethod
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "精製方法")
data class ProcessingMethodResponse(
    @Schema(description = "精製方法の値", example = "WASHED", requiredMode = Schema.RequiredMode.REQUIRED)
    val value: ProcessingMethod,
    @Schema(description = "精製方法の表示名", example = "ウォッシュド", requiredMode = Schema.RequiredMode.REQUIRED)
    val label: String,
) {
    companion object {
        fun from(processingMethod: ProcessingMethod): ProcessingMethodResponse = ProcessingMethodResponse(
            value = processingMethod,
            label = processingMethod.label,
        )
    }
}
