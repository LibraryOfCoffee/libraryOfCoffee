package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.usecase.FindShopsUsecase
import com.mametosho.cs.application.usecase.GetShopUsecase
import com.mametosho.cs.presentation.dto.response.ShopDetailResponse
import com.mametosho.cs.presentation.dto.response.ShopListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shops")
@Tag(name = "Shop", description = "店舗API")
class ShopController(
    private val findShopsUsecase: FindShopsUsecase,
    private val getShopUsecase: GetShopUsecase,
) {
    @GetMapping
    @Operation(
        summary = "店舗一覧取得",
        description = "店舗の一覧をページネーション付きで取得します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ShopListResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "items": [
                                        {
                                          "id": "00000000-0000-4000-8000-000000000031",
                                          "name": "珈琲工房 まめとしょ",
                                          "introduction": "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
                                          "shopUrl": "https://mametosho.example.com",
                                          "prefecture": "TOKYO",
                                          "logoImageUrl": "https://placehold.jp/100x100.png"
                                        },
                                        {
                                          "id": "00000000-0000-4000-8000-000000000032",
                                          "name": "CAFÉ LUMIÈRE",
                                          "introduction": "京都の町家を改装した珈琲専門店。フレンチローストからライトローストまで幅広い焙煎度をご用意。",
                                          "shopUrl": "https://cafe-lumiere.example.com",
                                          "prefecture": "KYOTO",
                                          "logoImageUrl": "https://placehold.jp/100x100.png"
                                        }
                                      ],
                                      "totalCount": 3,
                                      "page": 0,
                                      "size": 20
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun listShops(
        @Parameter(description = "ページ番号（0始まり）", required = true, example = "0")
        @RequestParam page: Int,
        @Parameter(description = "1ページあたりの件数", required = true, example = "20")
        @RequestParam size: Int,
    ): ResponseEntity<ShopListResponse> {
        val result = findShopsUsecase.execute(page, size)
        return ResponseEntity.ok(ShopListResponse.from(result))
    }

    @GetMapping("/{shopId}")
    @Operation(
        summary = "店舗詳細取得",
        description = "指定されたIDの店舗の詳細情報を取得します。参画中の店舗のみ取得できます。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ShopDetailResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "id": "00000000-0000-4000-8000-000000000031",
                                      "name": "珈琲工房 まめとしょ",
                                      "introduction": "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
                                      "particular": "厳選された豆のみを使用しています。",
                                      "shopUrl": "https://mametosho.example.com",
                                      "prefecture": "TOKYO",
                                      "images": [
                                        {
                                          "id": "00000000-0000-4000-8000-000000000010",
                                          "type": "LOGO",
                                          "imageUrl": "https://placehold.jp/100x100.png"
                                        },
                                        {
                                          "id": "00000000-0000-4000-8000-000000000011",
                                          "type": "MAIN",
                                          "imageUrl": "https://placehold.jp/600x400.png"
                                        }
                                      ]
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "店舗IDの形式が不正",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "bad_request",
                                summary = "店舗IDがUUID形式でない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "path": "/api/shops/invalid-id"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "店舗が見つかりません（存在しない、または参画中でない）",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "店舗が見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/shops/00000000-0000-4000-8000-000000000999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getShop(
        @Parameter(description = "店舗ID", required = true, example = "00000000-0000-4000-8000-000000000031")
        @PathVariable shopId: String,
    ): ResponseEntity<ShopDetailResponse> {
        val shop = getShopUsecase.execute(shopId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ShopDetailResponse.from(shop))
    }
}
