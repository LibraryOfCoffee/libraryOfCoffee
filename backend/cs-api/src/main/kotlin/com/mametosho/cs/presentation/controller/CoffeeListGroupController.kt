package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.usecase.FindCoffeeListGroupDetailUsecase
import com.mametosho.cs.presentation.dto.response.CoffeeListGroupDetailResponse
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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coffee-list-groups")
@Tag(name = "CoffeeListGroup", description = "珈琲リストグループAPI")
class CoffeeListGroupController(
    private val findCoffeeListGroupDetailUsecase: FindCoffeeListGroupDetailUsecase,
) {
    @GetMapping("/{coffeeListGroupId}")
    @Operation(
        summary = "珈琲リストグループ詳細取得",
        description = "指定されたIDの珈琲リストグループの詳細を取得します。グループに含まれる珈琲豆の情報（画像・テイスト評価を含む）を一括で返します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        schema = Schema(implementation = CoffeeListGroupDetailResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "id": "550e8400-e29b-41d4-a716-446655440001",
                                      "description": "2026年2月のおすすめ珈琲豆",
                                      "coffeeBeans": [
                                        {
                                          "id": "6ba7b810-9dad-41d4-80b5-246655440001",
                                          "name": "エチオピア イルガチェフェ G1",
                                          "description": "花のような華やかなフレーバーと、柑橘系の明るい酸味が特徴。クリーンカップで後味もすっきり。",
                                          "origin": "エチオピア",
                                          "farm": "イルガチェフェ コチャレ地区",
                                          "roastLevel": "light",
                                          "processingMethod": "washed",
                                          "images": [
                                            { "id": "7cc7c820-ae3e-41d4-90c6-346655440001", "type": "main", "imageUrl": "https://placehold.jp/150x150.png" }
                                          ],
                                          "tastes": [
                                            { "id": "8dd8d830-bf4f-41d4-a0d7-446655440001", "tasteId": "9ee9e940-c050-41d4-b0e8-546655440001", "tasteName": "酸味", "evaluationValue": 5 },
                                            { "id": "8dd8d830-bf4f-41d4-a0d7-446655440002", "tasteId": "9ee9e940-c050-41d4-b0e8-546655440002", "tasteName": "苦味", "evaluationValue": 1 },
                                            { "id": "8dd8d830-bf4f-41d4-a0d7-446655440003", "tasteId": "9ee9e940-c050-41d4-b0e8-546655440003", "tasteName": "甘味", "evaluationValue": 3 },
                                            { "id": "8dd8d830-bf4f-41d4-a0d7-446655440004", "tasteId": "9ee9e940-c050-41d4-b0e8-546655440004", "tasteName": "コク", "evaluationValue": 3 },
                                            { "id": "8dd8d830-bf4f-41d4-a0d7-446655440005", "tasteId": "9ee9e940-c050-41d4-b0e8-546655440005", "tasteName": "香り", "evaluationValue": 5 }
                                          ]
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
                description = "珈琲リストグループが見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "リソースが見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/coffee-list-groups/550e8400-e29b-41d4-a716-446655440099"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getCoffeeListGroupDetail(
        @Parameter(description = "珈琲リストグループID", required = true, example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable coffeeListGroupId: String,
    ): ResponseEntity<CoffeeListGroupDetailResponse> {
        val result = findCoffeeListGroupDetailUsecase.execute(coffeeListGroupId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(CoffeeListGroupDetailResponse.from(result))
    }
}
