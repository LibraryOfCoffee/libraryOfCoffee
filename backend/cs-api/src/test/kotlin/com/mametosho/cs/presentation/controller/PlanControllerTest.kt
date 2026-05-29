package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.query.PlanQueryService
import com.mametosho.cs.application.query.result.PlanListResult
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanControllerTest {

    private val sampleResult = listOf(
        PlanListResult(
            id = "00000000-0000-4000-8000-000000000024",
            label = "定番",
            gramWeight = 30,
            beanQuantity = 4,
            price = 1950,
            type = "SUBSCRIPTION",
            isRecommended = true,
        ),
    )

    private fun createController(
        result: List<PlanListResult> = sampleResult,
    ): PlanController {
        val fakeQueryService = object : PlanQueryService {
            override fun findList(): List<PlanListResult> = result
        }
        return PlanController(fakeQueryService)
    }

    @Nested
    inner class プラン一覧取得 {
        @Test
        fun `正常にプラン一覧を取得すると200が返る`() {
            val controller = createController()

            val response = controller.listPlans()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.size)
        }

        @Test
        fun `レスポンスボディのプランフィールドが正しい`() {
            val controller = createController()

            val response = controller.listPlans()

            val item = response.body?.get(0)
            assertEquals("00000000-0000-4000-8000-000000000024", item?.id)
            assertEquals("定番", item?.label)
            assertEquals(30, item?.gramWeight)
            assertEquals(4, item?.beanQuantity)
            assertEquals(1950, item?.price)
            assertEquals("SUBSCRIPTION", item?.type)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val controller = createController(result = emptyList())

            val response = controller.listPlans()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.size)
        }
    }
}
