package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.FindTastesUsecase
import com.mametosho.admin.presentation.dto.response.TasteResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/tastes")
@Tag(name = "Taste", description = "テイストAPI")
class TasteController(
    private val findTastesUsecase: FindTastesUsecase,
) {
    @GetMapping
    @Operation(
        summary = "テイスト一覧取得",
        description = "テイスト（酸味・苦味・甘味・コク・香りなど）の一覧を取得します。",
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
                                    [
                                      {
                                        "id": "00000000-0000-4000-8000-000000000041",
                                        "name": "酸味"
                                      },
                                      {
                                        "id": "00000000-0000-4000-8000-000000000042",
                                        "name": "苦味"
                                      },
                                      {
                                        "id": "00000000-0000-4000-8000-000000000043",
                                        "name": "甘味"
                                      },
                                      {
                                        "id": "00000000-0000-4000-8000-000000000044",
                                        "name": "コク"
                                      },
                                      {
                                        "id": "00000000-0000-4000-8000-000000000045",
                                        "name": "香り"
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
    fun listTastes(): ResponseEntity<List<TasteResponse>> {
        val results = findTastesUsecase.execute()
        return ResponseEntity.ok(results.map { TasteResponse.from(it) })
    }
}
