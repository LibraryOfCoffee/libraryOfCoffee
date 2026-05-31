package com.mametosho.admin.presentation.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "ログインレスポンス")
data class LoginResponse(
    @Schema(description = "アクセストークン", example = "eyJhbGciOiJIUzI1NiJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    val accessToken: String,

    @Schema(description = "トークンタイプ", example = "Bearer", requiredMode = Schema.RequiredMode.REQUIRED)
    val tokenType: String = "Bearer",

    @Schema(description = "有効期限（秒）", example = "3600", requiredMode = Schema.RequiredMode.REQUIRED)
    val expiresIn: Long,
)
