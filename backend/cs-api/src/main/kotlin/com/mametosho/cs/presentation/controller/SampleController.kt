package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.usecase.FindSampleDetailUsecase
import com.mametosho.cs.presentation.dto.response.SampleResponse
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
@RequestMapping("/api/samples")
@Tag(name = "Sample", description = "サンプルAPI")
class SampleController(
    private val findSampleDetailUsecase: FindSampleDetailUsecase
) {
    @GetMapping("/{id}")
    @Operation(
        summary = "サンプル取得",
        description = "指定されたIDのサンプルを取得します"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        schema = Schema(implementation = SampleResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "id": 1,
                                      "name": "コーヒー豆A"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "サンプルが見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "サンプルが見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/samples/999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getSampleById(
        @Parameter(description = "サンプルID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<SampleResponse> {
        val sample = findSampleDetailUsecase.execute(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SampleResponse.from(sample))
    }
}
