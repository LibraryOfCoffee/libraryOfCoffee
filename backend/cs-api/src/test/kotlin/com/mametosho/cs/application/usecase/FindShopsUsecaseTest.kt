package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.ShopQueryService
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.cs.application.query.result.ShopListResult
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class FindShopsUsecaseTest {

    private var capturedPage: Int? = null
    private var capturedSize: Int? = null

    private val sampleResult = PagedResult(
        items = listOf(
            ShopListResult(
                id = "00000000-0000-4000-8000-000000000031",
                name = "珈琲工房 まめとしょ",
                introduction = "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
                shopUrl = "https://mametosho.example.com",
                prefecture = "TOKYO",
                logoImageUrl = "https://placehold.jp/100x100.png",
            ),
        ),
        totalCount = 1L,
        page = 0,
        size = 20,
    )

    private fun createUsecase(
        result: PagedResult<ShopListResult> = sampleResult,
    ): FindShopsUsecase {
        val fakeQueryService = object : ShopQueryService {
            override fun findList(page: Int, size: Int): PagedResult<ShopListResult> {
                capturedPage = page
                capturedSize = size
                return result
            }
        }
        return FindShopsUsecase(fakeQueryService)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `pageとsizeを指定して一覧を取得できる`() {
            val usecase = createUsecase()

            val result = usecase.execute(page = 0, size = 20)

            assertEquals(1, result.items.size)
            assertEquals(0, capturedPage)
            assertEquals(20, capturedSize)
        }

        @Test
        fun `ページネーションパラメータがQueryServiceに正しく渡される`() {
            val usecase = createUsecase()

            usecase.execute(page = 2, size = 10)

            assertEquals(2, capturedPage)
            assertEquals(10, capturedSize)
        }
    }
}
