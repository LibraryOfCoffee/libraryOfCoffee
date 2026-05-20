package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.query.CoffeeBeanQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.cs.application.usecase.FindCoffeeBeansUsecase
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeBeanControllerTest {

    private val sampleResult = PagedResult(
        items = listOf(
            CoffeeBeanListResult(
                id = "00000000-0000-4000-8000-000000000071",
                name = "エチオピア イルガチェフェ G1",
                origin = "エチオピア",
                roastLevel = "LIGHT",
                processingMethod = "WASHED",
                isSpecialty = true,
            ),
        ),
        totalCount = 1L,
        page = 0,
        size = 20,
    )

    private fun createController(
        result: PagedResult<CoffeeBeanListResult> = sampleResult,
    ): CoffeeBeanController {
        val fakeQueryService = object : CoffeeBeanQueryService {
            override fun findList(
                page: Int,
                size: Int,
                origin: String?,
                roastLevel: RoastLevel?,
                processingMethod: ProcessingMethod?,
            ): PagedResult<CoffeeBeanListResult> = result
        }
        val fakeUsecase = object : FindCoffeeBeansUsecase(fakeQueryService) {
            override fun execute(
                page: Int,
                size: Int,
                origin: String?,
                roastLevel: RoastLevel?,
                processingMethod: ProcessingMethod?,
            ): PagedResult<CoffeeBeanListResult> = result
        }
        return CoffeeBeanController(fakeUsecase)
    }

    @Nested
    inner class 珈琲豆一覧取得 {
        @Test
        fun `正常に珈琲豆一覧を取得すると200が返る`() {
            val controller = createController()

            val response = controller.listCoffeeBeans(
                page = 0,
                size = 20,
                origin = null,
                roastLevel = null,
                processingMethod = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `レスポンスボディの珈琲豆フィールドが正しい`() {
            val controller = createController()

            val response = controller.listCoffeeBeans(
                page = 0,
                size = 20,
                origin = null,
                roastLevel = null,
                processingMethod = null,
            )

            val item = response.body?.items?.get(0)
            assertEquals("00000000-0000-4000-8000-000000000071", item?.id)
            assertEquals("エチオピア イルガチェフェ G1", item?.name)
            assertEquals("エチオピア", item?.origin)
            assertEquals("LIGHT", item?.roastLevel)
            assertEquals("WASHED", item?.processingMethod)
            assertEquals(true, item?.isSpecialty)
        }

        @Test
        fun `originフィルタを指定して取得できる`() {
            val controller = createController()

            val response = controller.listCoffeeBeans(
                page = 0,
                size = 20,
                origin = "エチオピア",
                roastLevel = null,
                processingMethod = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
        }

        @Test
        fun `roastLevelフィルタを指定して取得できる`() {
            val controller = createController()

            val response = controller.listCoffeeBeans(
                page = 0,
                size = 20,
                origin = null,
                roastLevel = RoastLevel.LIGHT,
                processingMethod = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
        }

        @Test
        fun `processingMethodフィルタを指定して取得できる`() {
            val controller = createController()

            val response = controller.listCoffeeBeans(
                page = 0,
                size = 20,
                origin = null,
                roastLevel = null,
                processingMethod = ProcessingMethod.WASHED,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val emptyResult = PagedResult<CoffeeBeanListResult>(
                items = emptyList(),
                totalCount = 0L,
                page = 0,
                size = 20,
            )
            val controller = createController(result = emptyResult)

            val response = controller.listCoffeeBeans(
                page = 0,
                size = 20,
                origin = null,
                roastLevel = null,
                processingMethod = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }
    }
}
