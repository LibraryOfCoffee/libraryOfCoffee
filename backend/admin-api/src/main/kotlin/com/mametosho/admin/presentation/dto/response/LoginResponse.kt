package com.mametosho.admin.presentation.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "ログインレスポンス")
data class LoginResponse(
    @Schema(description = "アクセストークン", example = "eyJhbGciOiJIUzI1NiJ9...")
    val accessToken: String,

    @Schema(description = "トークンタイプ", example = "Bearer")
    val tokenType: String = "Bearer",

    @Schema(description = "有効期限（秒）", example = "3600")
    val expiresIn: Long,
)
