package com.mametosho.admin.presentation.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "ログインリクエスト")
data class LoginRequest(
    @Schema(description = "メールアドレス", example = "admin@mametosho.com")
    val email: String,

    @Schema(description = "パスワード", example = "password123")
    val password: String,
)
