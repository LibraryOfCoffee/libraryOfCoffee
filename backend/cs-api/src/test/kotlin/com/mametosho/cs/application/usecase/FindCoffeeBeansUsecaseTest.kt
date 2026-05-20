package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.CoffeeBeanQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class FindCoffeeBeansUsecaseTest {

    private var capturedPage: Int? = null
    private var capturedSize: Int? = null
    private var capturedOrigin: String? = null
    private var capturedRoastLevel: RoastLevel? = null
    private var capturedProcessingMethod: ProcessingMethod? = null

    private val sampleResult = PagedResult(
        items = listOf(
            CoffeeBeanListResult(
                id = "00000000-0000-4000-8000-000000000001",
                name = "テストコーヒー豆",
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

    private fun createUsecase(
        result: PagedResult<CoffeeBeanListResult> = sampleResult,
    ): FindCoffeeBeansUsecase {
        val fakeQueryService = object : CoffeeBeanQueryService {
            override fun findList(
                page: Int,
                size: Int,
                origin: String?,
                roastLevel: RoastLevel?,
                processingMethod: ProcessingMethod?,
            ): PagedResult<CoffeeBeanListResult> {
                capturedPage = page
                capturedSize = size
                capturedOrigin = origin
                capturedRoastLevel = roastLevel
                capturedProcessingMethod = processingMethod
                return result
            }
        }
        return FindCoffeeBeansUsecase(fakeQueryService)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `フィルタなしで一覧を取得できる`() {
            val usecase = createUsecase()

            val result = usecase.execute(page = 0, size = 20, origin = null, roastLevel = null, processingMethod = null)

            assertEquals(1, result.items.size)
            assertEquals(0, capturedPage)
            assertEquals(20, capturedSize)
            assertEquals(null, capturedOrigin)
            assertEquals(null, capturedRoastLevel)
            assertEquals(null, capturedProcessingMethod)
        }

        @Test
        fun `originを指定して取得できる`() {
            val usecase = createUsecase()

            usecase.execute(page = 0, size = 20, origin = "エチオピア", roastLevel = null, processingMethod = null)

            assertEquals("エチオピア", capturedOrigin)
        }

        @Test
        fun `roastLevelを指定して取得できる`() {
            val usecase = createUsecase()

            usecase.execute(page = 0, size = 20, origin = null, roastLevel = RoastLevel.LIGHT, processingMethod = null)

            assertEquals(RoastLevel.LIGHT, capturedRoastLevel)
        }

        @Test
        fun `processingMethodを指定して取得できる`() {
            val usecase = createUsecase()

            usecase.execute(page = 0, size = 20, origin = null, roastLevel = null, processingMethod = ProcessingMethod.WASHED)

            assertEquals(ProcessingMethod.WASHED, capturedProcessingMethod)
        }

        @Test
        fun `全フィルタを組み合わせて取得できる`() {
            val usecase = createUsecase()

            usecase.execute(
                page = 1,
                size = 10,
                origin = "エチオピア",
                roastLevel = RoastLevel.LIGHT,
                processingMethod = ProcessingMethod.WASHED,
            )

            assertEquals(1, capturedPage)
            assertEquals(10, capturedSize)
            assertEquals("エチオピア", capturedOrigin)
            assertEquals(RoastLevel.LIGHT, capturedRoastLevel)
            assertEquals(ProcessingMethod.WASHED, capturedProcessingMethod)
        }
    }
}
