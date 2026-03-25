package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.CreateShopUsecase
import com.mametosho.admin.application.usecase.DeleteShopUsecase
import com.mametosho.admin.application.usecase.GetShopUsecase
import com.mametosho.admin.application.usecase.ListShopsUsecase
import com.mametosho.admin.application.usecase.UpdateShopUsecase
import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.admin.presentation.dto.request.UpdateShopRequest
import com.mametosho.admin.presentation.dto.response.ErrorResponse
import com.mametosho.admin.presentation.dto.response.PagedResponse
import com.mametosho.admin.presentation.dto.response.ShopDetailResponse
import com.mametosho.admin.presentation.dto.response.ShopListResponse
import com.mametosho.admin.presentation.dto.response.ShopResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/admin/shops")
@Tag(name = "Shop", description = "店舗API")
class ShopController(
    private val getShopUsecase: GetShopUsecase,
    private val listShopsUsecase: ListShopsUsecase,
    private val createShopUsecase: CreateShopUsecase,
    private val updateShopUsecase: UpdateShopUsecase,
    private val deleteShopUsecase: DeleteShopUsecase,
) {
    @GetMapping
    @Operation(
        summary = "店舗一覧取得",
        description = "店舗の一覧をページネーション付きで取得します。店名による部分一致検索が可能です。画像は含まれません。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "items": [
                                        {
                                          "id": "00000000-0000-4000-8000-000000000001",
                                          "shopifyShopId": "test-shop-001",
                                          "name": "テスト珈琲店",
                                          "introduction": "こだわりの珈琲をお届けします。",
                                          "particular": "厳選された豆のみを使用しています。",
                                          "shopUrl": "https://example.com"
                                        }
                                      ],
                                      "totalCount": 1,
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
        @Parameter(description = "ページ番号（0始まり）", example = "0")
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "1ページあたりの件数", example = "20")
        @RequestParam(defaultValue = "20") size: Int,
        @Parameter(description = "店名（部分一致検索）", example = "珈琲", required = false)
        @RequestParam(required = false) name: String?,
    ): ResponseEntity<PagedResponse<ShopListResponse>> {
        val result = listShopsUsecase.execute(page, size, name)
        return ResponseEntity.ok(PagedResponse.from(result) { ShopListResponse.from(it) })
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "店舗詳細取得",
        description = "指定されたIDの店舗の詳細情報を取得します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        schema = Schema(implementation = ShopDetailResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "id": "00000000-0000-4000-8000-000000000001",
                                      "shopifyShopId": "test-shop-001",
                                      "name": "テスト珈琲店",
                                      "introduction": "こだわりの珈琲をお届けします。",
                                      "particular": "厳選された豆のみを使用しています。",
                                      "shopUrl": "https://example.com",
                                      "images": [
                                        {
                                          "id": "00000000-0000-4000-8000-000000000010",
                                          "type": "MAIN",
                                          "imageUrl": "https://example.com/shop-image.jpg"
                                        },
                                        {
                                          "id": "00000000-0000-4000-8000-000000000011",
                                          "type": "LOGO",
                                          "imageUrl": "https://example.com/shop-logo.png"
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
                responseCode = "404",
                description = "店舗が見つかりません",
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
                                      "path": "/api/admin/shops/00000000-0000-4000-8000-000000000999"
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
        @Parameter(description = "店舗ID", required = true, example = "00000000-0000-4000-8000-000000000001")
        @PathVariable id: String,
    ): ResponseEntity<ShopDetailResponse> {
        val shop = getShopUsecase.execute(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ShopDetailResponse.from(shop))
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "店舗登録",
        description = "新しい店舗を登録します。multipart/form-data形式で送信してください。ShopIdおよびShopImageIdはサーバー側でUUIDv4を自動生成します。",
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
    fun createShop(
        @RequestPart("data") request: CreateShopRequest,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
        @RequestParam("imageTypes", required = false) imageTypes: List<String>?,
    ): ResponseEntity<ShopResponse> {
        val shop = createShopUsecase.execute(request, images ?: emptyList(), imageTypes ?: emptyList())
        return ResponseEntity.status(HttpStatus.CREATED).body(ShopResponse.from(shop))
    }

    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "店舗編集",
        description = "指定されたIDの店舗を編集します。multipart/form-data形式で送信してください。全項目を置換します。ShopImageIdはサーバー側でUUIDv4を自動再生成します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "編集成功",
                content = [
                    Content(
                        schema = Schema(implementation = ShopResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "編集成功例",
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
                                      "path": "/api/admin/shops/00000000-0000-4000-8000-000000000001"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "店舗が見つかりません",
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
                                      "path": "/api/admin/shops/00000000-0000-4000-8000-999999999999"
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
                                      "path": "/api/admin/shops/00000000-0000-4000-8000-000000000001"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun updateShop(
        @Parameter(description = "店舗ID", required = true, example = "00000000-0000-4000-8000-000000000001")
        @PathVariable id: String,
        @RequestPart("data") request: UpdateShopRequest,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
        @RequestParam("imageTypes", required = false) imageTypes: List<String>?,
    ): ResponseEntity<ShopResponse> {
        val shop = updateShopUsecase.execute(id, request, images ?: emptyList(), imageTypes ?: emptyList())
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ShopResponse.from(shop))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "店舗削除",
        description = "指定されたIDの店舗を削除します。店舗画像も同時に削除されます。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "削除成功",
            ),
            ApiResponse(
                responseCode = "404",
                description = "店舗が見つかりません",
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
                                      "path": "/api/admin/shops/00000000-0000-4000-8000-999999999999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "関連するコーヒー豆が存在するため削除できません",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [
                            ExampleObject(
                                name = "conflict",
                                summary = "関連するコーヒー豆が存在する場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "path": "/api/admin/shops/00000000-0000-4000-8000-000000000001"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun deleteShop(
        @Parameter(description = "店舗ID", required = true, example = "00000000-0000-4000-8000-000000000001")
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        val deleted = deleteShopUsecase.execute(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
