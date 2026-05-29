package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.usecase.FindShopsUsecase
import com.mametosho.cs.presentation.dto.response.PagedResponse
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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shops")
@Tag(name = "Shop", description = "店舗API")
class ShopController(
    private val findShopsUsecase: FindShopsUsecase,
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
    ): ResponseEntity<PagedResponse<ShopListResponse>> {
        val result = findShopsUsecase.execute(page, size)
        return ResponseEntity.ok(PagedResponse.from(result) { ShopListResponse.from(it) })
    }
}
