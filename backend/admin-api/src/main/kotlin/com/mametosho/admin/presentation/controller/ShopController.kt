package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.CreateShopUsecase
import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.admin.presentation.dto.response.ErrorResponse
import com.mametosho.admin.presentation.dto.response.ShopResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/shops")
@Tag(name = "Shop", description = "店舗API")
class ShopController(
    private val createShopUsecase: CreateShopUsecase,
) {
    @PostMapping
    @Operation(
        summary = "店舗登録",
        description = "新しい店舗を登録します。ShopIdおよびShopImageIdはサーバー側でUUIDv4を自動生成します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "登録成功",
                content = [
                    Content(
                        schema = Schema(implementation = ShopResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "登録成功例",
                                value = """
                                    {
                                      "id": "00000000-0000-4000-8000-000000000001"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "バリデーションエラー",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [
                            ExampleObject(
                                name = "bad_request",
                                summary = "バリデーションエラーの場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "path": "/api/admin/shops"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "ShopifyショップIDが重複しています",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [
                            ExampleObject(
                                name = "conflict",
                                summary = "ShopifyショップIDが重複している場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "path": "/api/admin/shops"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    // TODO: 画像はURLではなくファイルアップロード形式に変更する（マルチパート対応 + ストレージ保存）
    fun createShop(
        @RequestBody request: CreateShopRequest,
    ): ResponseEntity<ShopResponse> {
        val shop = createShopUsecase.execute(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ShopResponse.from(shop))
    }
}
