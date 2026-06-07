package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.service.buildImageUploads
import com.mametosho.admin.application.usecase.CreateCoffeeBeanUsecase
import com.mametosho.admin.application.usecase.DeleteCoffeeBeanUsecase
import com.mametosho.admin.application.usecase.GetCoffeeBeanUsecase
import com.mametosho.admin.application.usecase.FindCoffeeBeansUsecase
import com.mametosho.admin.application.usecase.UpdateCoffeeBeanUsecase
import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.admin.presentation.dto.request.UpdateCoffeeBeanRequest
import com.mametosho.admin.presentation.dto.response.CoffeeBeanDetailResponse
import com.mametosho.admin.presentation.dto.response.CoffeeBeanListResponse
import com.mametosho.admin.presentation.dto.response.CoffeeBeanSummaryResponse
import com.mametosho.admin.presentation.dto.response.CoffeeBeanResponse
import com.mametosho.admin.presentation.dto.response.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import io.swagger.v3.oas.annotations.Parameter
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
@RequestMapping("/api/admin/coffee-beans")
@Tag(name = "CoffeeBean", description = "コーヒー豆API")
class CoffeeBeanController(
    private val getCoffeeBeanUsecase: GetCoffeeBeanUsecase,
    private val findCoffeeBeansUsecase: FindCoffeeBeansUsecase,
    private val createCoffeeBeanUsecase: CreateCoffeeBeanUsecase,
    private val updateCoffeeBeanUsecase: UpdateCoffeeBeanUsecase,
    private val deleteCoffeeBeanUsecase: DeleteCoffeeBeanUsecase,
) {
    @GetMapping
    @Operation(
        summary = "コーヒー豆一覧取得",
        description = "コーヒー豆の一覧をページネーション付きで取得します。画像・テイスト評価は含まれません。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        schema = Schema(implementation = CoffeeBeanListResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "items": [
                                        {
                                          "id": "00000000-0000-4000-8000-000000000001",
                                          "shopId": "00000000-0000-4000-8000-000000000002",
                                          "shopifyBeanId": "test-bean-001",
                                          "name": "エチオピア イルガチェフェ",
                                          "description": "フルーティーな香りが特徴的なコーヒー豆です。",
                                          "origin": "エチオピア",
                                          "farm": "イルガチェフェ農園",
                                          "roastLevel": "MEDIUM",
                                          "processingMethod": "WASHED",
                                          "isSpecialty": true,
                                          "publishStatus": "PUBLISHED"
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
    fun listCoffeeBeans(
        @Parameter(description = "ページ番号（0始まり）", example = "0")
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "1ページあたりの件数", example = "20")
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CoffeeBeanListResponse> {
        val result = findCoffeeBeansUsecase.execute(page, size)
        return ResponseEntity.ok(CoffeeBeanListResponse.from(result))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "コーヒー豆詳細取得",
        description = "指定されたIDのコーヒー豆の詳細情報を取得します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        schema = Schema(implementation = CoffeeBeanDetailResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "id": "00000000-0000-4000-8000-000000000001",
                                      "shopId": "00000000-0000-4000-8000-000000000002",
                                      "shopifyBeanId": "test-bean-001",
                                      "name": "エチオピア イルガチェフェ",
                                      "description": "フルーティーな香りが特徴的なコーヒー豆です。",
                                      "origin": "エチオピア",
                                      "farm": "イルガチェフェ農園",
                                      "roastLevel": "MEDIUM",
                                      "processingMethod": "WASHED",
                                      "isSpecialty": true,
                                      "publishStatus": "PUBLISHED",
                                      "images": [
                                        {
                                          "id": "00000000-0000-4000-8000-000000000010",
                                          "type": "MAIN",
                                          "imageUrl": "https://example.com/image.jpg"
                                        }
                                      ],
                                      "tastes": [
                                        {
                                          "id": "00000000-0000-4000-8000-000000000020",
                                          "tasteName": "酸味",
                                          "evaluationValue": 4
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
                description = "コーヒー豆が見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "コーヒー豆が見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/admin/coffee-beans/00000000-0000-4000-8000-000000000999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getCoffeeBean(
        @Parameter(description = "コーヒー豆ID", required = true, example = "00000000-0000-4000-8000-000000000001")
        @PathVariable id: String,
    ): ResponseEntity<CoffeeBeanDetailResponse> {
        val result = getCoffeeBeanUsecase.execute(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(CoffeeBeanDetailResponse.from(result))
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "コーヒー豆登録",
        description = "新しいコーヒー豆を登録します。multipart/form-data形式で送信してください。" +
            "CoffeeBeanId、CoffeeBeanImageId、CoffeeBeanTasteIdはサーバー側でUUIDv4を自動生成します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "登録成功",
                content = [
                    Content(
                        schema = Schema(implementation = CoffeeBeanResponse::class),
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
                                      "path": "/api/admin/coffee-beans"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Shopify商品IDが重複しています",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [
                            ExampleObject(
                                name = "conflict",
                                summary = "Shopify商品IDが重複している場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "path": "/api/admin/coffee-beans"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun createCoffeeBean(
        @RequestPart("data") request: CreateCoffeeBeanRequest,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
    ): ResponseEntity<CoffeeBeanResponse> {
        val uploads = buildImageUploads(images ?: emptyList(), request.imageTypes)
        val coffeeBean = createCoffeeBeanUsecase.execute(request, uploads)
        return ResponseEntity.status(HttpStatus.CREATED).body(CoffeeBeanResponse.from(coffeeBean))
    }

    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "コーヒー豆更新",
        description = "指定されたIDのコーヒー豆を更新します。multipart/form-data形式で送信してください。画像・テイスト評価は全件置換されます。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "更新成功",
                content = [
                    Content(
                        schema = Schema(implementation = CoffeeBeanResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "更新成功例",
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
                                      "path": "/api/admin/coffee-beans/00000000-0000-4000-8000-000000000001"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "コーヒー豆が見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "コーヒー豆が見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/admin/coffee-beans/00000000-0000-4000-8000-000000000999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Shopify商品IDが重複しています",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [
                            ExampleObject(
                                name = "conflict",
                                summary = "Shopify商品IDが重複している場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "path": "/api/admin/coffee-beans/00000000-0000-4000-8000-000000000001"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun updateCoffeeBean(
        @Parameter(description = "コーヒー豆ID", required = true, example = "00000000-0000-4000-8000-000000000001")
        @PathVariable id: String,
        @RequestPart("data") request: UpdateCoffeeBeanRequest,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
    ): ResponseEntity<CoffeeBeanResponse> {
        val uploads = buildImageUploads(images ?: emptyList(), request.imageTypes)
        val coffeeBean = updateCoffeeBeanUsecase.execute(id, request, uploads, request.keepImageIds)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(CoffeeBeanResponse.from(coffeeBean))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "コーヒー豆削除",
        description = "指定されたIDのコーヒー豆を削除します。関連する画像・テイスト評価も同時に削除されます。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "削除成功",
                content = [Content()],
            ),
            ApiResponse(
                responseCode = "404",
                description = "コーヒー豆が見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "コーヒー豆が見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/admin/coffee-beans/00000000-0000-4000-8000-000000000999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun deleteCoffeeBean(
        @Parameter(description = "コーヒー豆ID", required = true, example = "00000000-0000-4000-8000-000000000001")
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        val deleted = deleteCoffeeBeanUsecase.execute(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
