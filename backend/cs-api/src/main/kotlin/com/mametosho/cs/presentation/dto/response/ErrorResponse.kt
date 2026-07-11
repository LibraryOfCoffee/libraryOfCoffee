package com.mametosho.cs.presentation.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

@Schema(description = "エラーレスポンス")
data class ErrorResponse(
    @Schema(description = "エラー発生日時", example = "2026-02-23T12:00:00.000+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    val timestamp: OffsetDateTime,

    @Schema(description = "HTTPステータスコード", example = "404", requiredMode = Schema.RequiredMode.REQUIRED)
    val status: Int,

    @Schema(description = "エラー概要", example = "Not Found", requiredMode = Schema.RequiredMode.REQUIRED)
    val error: String,

    @Schema(
        description = "リクエストパス",
        example = "/api/coffee-beans/00000000-0000-4000-8000-000000000099",
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val path: String,
)
