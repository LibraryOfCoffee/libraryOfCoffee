package com.mametosho.cs.presentation.dto.response

import com.mametosho.domain.model.Sample
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "サンプルレスポンス")
data class SampleResponse(
    @Schema(description = "サンプルID", example = "1")
    val id: Long,

    @Schema(description = "サンプル名", example = "コーヒー豆A")
    val name: String
) {
    companion object {
        fun from(sample: Sample): SampleResponse {
            return SampleResponse(
                id = sample.id,
                name = sample.name
            )
        }
    }
}
