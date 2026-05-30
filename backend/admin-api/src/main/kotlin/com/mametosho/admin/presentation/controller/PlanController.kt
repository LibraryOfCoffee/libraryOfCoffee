package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.FindPlansUsecase
import com.mametosho.admin.application.usecase.GetPlanUsecase
import com.mametosho.admin.application.usecase.UpdatePlanUsecase
import com.mametosho.admin.presentation.dto.request.UpdatePlanRequest
import com.mametosho.admin.presentation.dto.response.ErrorResponse
import com.mametosho.admin.presentation.dto.response.PagedResponse
import com.mametosho.admin.presentation.dto.response.PlanDetailResponse
import com.mametosho.admin.presentation.dto.response.PlanResponse
import com.mametosho.admin.presentation.dto.response.PlanSummaryResponse
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
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/plans")
@Tag(name = "Plan", description = "プランAPI")
class PlanController(
    private val findPlansUsecase: FindPlansUsecase,
    private val getPlanUsecase: GetPlanUsecase,
    private val updatePlanUsecase: UpdatePlanUsecase,
) {
    @GetMapping
    @Operation(
        summary = "プラン一覧取得",
        description = "プランの一覧をページネーション付きで取得します。プラン表示名による部分一致検索が可能です。",
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
                                          "id": "00000000-0000-4000-8000-000000000024",
                                          "shopifyPlanId": "test-plan-001",
                                          "label": "定番",
                                          "gramWeight": 60,
                                          "beanQuantity": 4,
                                          "price": 3800,
                                          "type": "SUBSCRIPTION",
                                          "isRecommended": true
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
    fun listPlans(
        @Parameter(description = "ページ番号（0始まり）", example = "0")
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "1ページあたりの件数", example = "20")
        @RequestParam(defaultValue = "20") size: Int,
        @Parameter(description = "プラン表示名（部分一致検索）", example = "定番", required = false)
        @RequestParam(required = false) keyword: String?,
    ): ResponseEntity<PagedResponse<PlanSummaryResponse>> {
        val result = findPlansUsecase.execute(page, size, keyword)
        return ResponseEntity.ok(PagedResponse.from(result) { PlanSummaryResponse.from(it) })
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "プラン詳細取得",
        description = "指定されたIDのプランの詳細情報を取得します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        schema = Schema(implementation = PlanDetailResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "id": "00000000-0000-4000-8000-000000000024",
                                      "shopifyPlanId": "test-plan-001",
                                      "label": "定番",
                                      "gramWeight": 60,
                                      "beanQuantity": 4,
                                      "price": 3800,
                                      "type": "SUBSCRIPTION",
                                      "isRecommended": true
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "プランが見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "プランが見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/admin/plans/00000000-0000-4000-8000-000000000999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getPlan(
        @Parameter(description = "プランID", required = true, example = "00000000-0000-4000-8000-000000000024")
        @PathVariable id: String,
    ): ResponseEntity<PlanDetailResponse> {
        val plan = getPlanUsecase.execute(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(PlanDetailResponse.from(plan))
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "プラン編集",
        description = "指定されたIDのプランを編集します。全項目を置換します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "編集成功",
                content = [
                    Content(
                        schema = Schema(implementation = PlanResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "編集成功例",
                                value = """
                                    {
                                      "id": "00000000-0000-4000-8000-000000000024"
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
                                      "path": "/api/admin/plans/00000000-0000-4000-8000-000000000024"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "プランが見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "プランが見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/admin/plans/00000000-0000-4000-8000-000000000999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "ShopifyプランIDが重複しています",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [
                            ExampleObject(
                                name = "conflict",
                                summary = "ShopifyプランIDが重複している場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "path": "/api/admin/plans/00000000-0000-4000-8000-000000000024"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun updatePlan(
        @Parameter(description = "プランID", required = true, example = "00000000-0000-4000-8000-000000000024")
        @PathVariable id: String,
        @RequestBody request: UpdatePlanRequest,
    ): ResponseEntity<PlanResponse> {
        val plan = updatePlanUsecase.execute(id, request)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(PlanResponse.from(plan))
    }
}
