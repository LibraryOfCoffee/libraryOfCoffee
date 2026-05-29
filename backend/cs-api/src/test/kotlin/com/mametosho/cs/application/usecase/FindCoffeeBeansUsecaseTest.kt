package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.CoffeeBeanQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.Prefecture
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class FindCoffeeBeansUsecaseTest {

    private var capturedPage: Int? = null
    private var capturedSize: Int? = null
    private var capturedOrigin: String? = null
    private var capturedRoastLevel: RoastLevel? = null
    private var capturedPrefecture: Prefecture? = null

    private val sampleResult = PagedResult(
        items = listOf(
            CoffeeBeanListResult(
                id = "00000000-0000-4000-8000-000000000001",
                name = "テストコーヒー豆",
                origin = "エチオピア",
                roastLevel = "LIGHT",
                processingMethod = "WASHED",
                isSpecialty = true,
                description = "テスト用の説明文です。",
                imageUrl = "https://example.com/images/test.jpg",
                shopName = "テスト珈琲焙煎所",
                tasteProfiles = listOf(
                    CoffeeBeanListResult.TasteProfileResult(name = "酸味", value = 60),
                ),
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
                prefecture: Prefecture?,
            ): PagedResult<CoffeeBeanListResult> {
                capturedPage = page
                capturedSize = size
                capturedOrigin = origin
                capturedRoastLevel = roastLevel
                capturedPrefecture = prefecture
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

            val result = usecase.execute(page = 0, size = 20, origin = null, roastLevel = null, prefecture = null)

            assertEquals(1, result.items.size)
            assertEquals(0, capturedPage)
            assertEquals(20, capturedSize)
            assertEquals(null, capturedOrigin)
            assertEquals(null, capturedRoastLevel)
            assertEquals(null, capturedPrefecture)
        }

        @Test
        fun `originを指定して取得できる`() {
            val usecase = createUsecase()

            usecase.execute(page = 0, size = 20, origin = "エチオピア", roastLevel = null, prefecture = null)

            assertEquals("エチオピア", capturedOrigin)
        }

        @Test
        fun `roastLevelを指定して取得できる`() {
            val usecase = createUsecase()

            usecase.execute(page = 0, size = 20, origin = null, roastLevel = RoastLevel.LIGHT, prefecture = null)

            assertEquals(RoastLevel.LIGHT, capturedRoastLevel)
        }

        @Test
        fun `prefectureを指定して取得できる`() {
            val usecase = createUsecase()

            usecase.execute(page = 0, size = 20, origin = null, roastLevel = null, prefecture = Prefecture.TOKYO)

            assertEquals(Prefecture.TOKYO, capturedPrefecture)
        }

        @Test
        fun `全フィルタを組み合わせて取得できる`() {
            val usecase = createUsecase()

            usecase.execute(
                page = 1,
                size = 10,
                origin = "エチオピア",
                roastLevel = RoastLevel.LIGHT,
                prefecture = Prefecture.TOKYO,
            )

            assertEquals(1, capturedPage)
            assertEquals(10, capturedSize)
            assertEquals("エチオピア", capturedOrigin)
            assertEquals(RoastLevel.LIGHT, capturedRoastLevel)
            assertEquals(Prefecture.TOKYO, capturedPrefecture)
        }
    }
}
