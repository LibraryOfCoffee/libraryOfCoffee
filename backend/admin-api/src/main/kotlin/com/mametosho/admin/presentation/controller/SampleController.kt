package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.FindSampleDetailUsecase
import com.mametosho.admin.presentation.dto.response.SampleResponse
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
@RequestMapping("/api/admin/samples")
@Tag(name = "Sample", description = "管理者用サンプルAPI")
class SampleController(
    private val findSampleDetailUsecase: FindSampleDetailUsecase
) {
    @GetMapping("/{id}")
    @Operation(
        summary = "サンプル取得",
        description = "指定されたIDのサンプルを取得します（管理者用）"
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
                                value = """{"id": 1, "name": "コーヒー豆A"}"""
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "サンプルが見つかりません",
                content = [Content()]
            )
        ]
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
