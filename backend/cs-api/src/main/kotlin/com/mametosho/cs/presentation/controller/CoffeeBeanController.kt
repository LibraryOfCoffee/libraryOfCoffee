package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.usecase.FindCoffeeBeansUsecase
import com.mametosho.cs.presentation.dto.response.CoffeeBeanListResponse
import com.mametosho.cs.presentation.dto.response.PagedResponse
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.Prefecture
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coffeeBeans")
@Tag(name = "CoffeeBean", description = "珈琲豆API")
class CoffeeBeanController(
    private val findCoffeeBeansUsecase: FindCoffeeBeansUsecase,
) {
    @GetMapping
    @Operation(
        summary = "珈琲豆一覧取得",
        description = "珈琲豆の一覧をページネーション付きで取得します。産地・焙煎度・精製方法でフィルタリングできます。",
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
                                          "id": "00000000-0000-4000-8000-000000000071",
                                          "name": "エチオピア イルガチェフェ G1",
                                          "origin": "エチオピア",
                                          "roastLevel": "LIGHT",
                                          "processingMethod": "WASHED",
                                          "isSpecialty": true
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
        @Parameter(description = "ページ番号（0始まり）", required = true, example = "0")
        @RequestParam page: Int,
        @Parameter(description = "1ページあたりの件数", required = true, example = "20")
        @RequestParam size: Int,
        @Parameter(description = "産地（部分一致）", example = "エチオピア")
        @RequestParam(required = false) origin: String?,
        @Parameter(description = "焙煎度", example = "LIGHT")
        @RequestParam(required = false) roastLevel: RoastLevel?,
        @Parameter(description = "ロースターの都道府県", example = "TOKYO")
        @RequestParam(required = false) prefecture: Prefecture?,
    ): ResponseEntity<PagedResponse<CoffeeBeanListResponse>> {
        val result = findCoffeeBeansUsecase.execute(page, size, origin, roastLevel, prefecture)
        return ResponseEntity.ok(PagedResponse.from(result) { CoffeeBeanListResponse.from(it) })
    }
}
