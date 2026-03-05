package com.mametosho.admin.presentation.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

@Schema(description = "エラーレスポンス")
data class ErrorResponse(
    @Schema(description = "エラー発生日時", example = "2026-02-23T12:00:00.000+00:00")
    val timestamp: OffsetDateTime,

    @Schema(description = "HTTPステータスコード", example = "404")
    val status: Int,

    @Schema(description = "エラー概要", example = "Not Found")
    val error: String,

    @Schema(description = "リクエストパス", example = "/api/admin/resources/550e8400-e29b-41d4-a716-446655440099")
    val path: String,
)
