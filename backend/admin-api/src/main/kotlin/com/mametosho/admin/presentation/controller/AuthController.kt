package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.LoginUsecase
import com.mametosho.admin.config.JwtProperties
import com.mametosho.admin.presentation.dto.request.LoginRequest
import com.mametosho.admin.presentation.dto.response.LoginResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/auth")
@Tag(name = "Auth", description = "認証API")
class AuthController(
    private val loginUsecase: LoginUsecase,
    private val jwtProperties: JwtProperties,
) {

    @PostMapping("/login")
    @Operation(
        summary = "ログイン",
        description = "メールアドレスとパスワードで認証し、JWTアクセストークンを返します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "認証成功",
                content = [
                    Content(
                        schema = Schema(implementation = LoginResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "認証成功例",
                                value = """
                                    {
                                      "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
                                      "tokenType": "Bearer",
                                      "expiresIn": 3600
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "認証失敗",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "unauthorized",
                                summary = "メールアドレスまたはパスワードが不正な場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "path": "/api/admin/auth/login"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        val token = loginUsecase.execute(request.email, request.password)

        return ResponseEntity.ok(
            LoginResponse(
                accessToken = token,
                expiresIn = jwtProperties.expirationSeconds,
            ),
        )
    }
}
