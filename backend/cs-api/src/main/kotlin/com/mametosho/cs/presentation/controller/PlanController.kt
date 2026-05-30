package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.usecase.FindPlansUsecase
import com.mametosho.cs.presentation.dto.response.PlanResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/plans")
@Tag(name = "Plan", description = "プランAPI")
class PlanController(
    private val findPlansUsecase: FindPlansUsecase,
) {
    @GetMapping
    @Operation(
        summary = "プラン一覧取得",
        description = "プランの一覧を取得します。",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = PlanResponse::class)),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    [
                                      {
                                        "id": "00000000-0000-4000-8000-000000000024",
                                        "label": "定番",
                                        "gramWeight": 30,
                                        "beanQuantity": 4,
                                        "price": 1950,
                                        "type": "SUBSCRIPTION",
                                        "isRecommended": true
                                      },
                                      {
                                        "id": "00000000-0000-4000-8000-00000000002d",
                                        "label": "定番",
                                        "gramWeight": 30,
                                        "beanQuantity": 4,
                                        "price": 2150,
                                        "type": "SINGLE",
                                        "isRecommended": true
                                      }
                                    ]
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun listPlans(): ResponseEntity<List<PlanResponse>> {
        val results = findPlansUsecase.execute()
        return ResponseEntity.ok(results.map { PlanResponse.from(it) })
    }
}
