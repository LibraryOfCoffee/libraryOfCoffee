package com.mametosho.cs.presentation.controller

import com.mametosho.cs.application.query.ShopQueryService
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.cs.application.query.result.ShopListResult
import com.mametosho.cs.application.usecase.FindShopsUsecase
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopControllerTest {

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

    private fun createController(
        result: PagedResult<ShopListResult> = sampleResult,
    ): ShopController {
        val fakeQueryService = object : ShopQueryService {
            override fun findList(page: Int, size: Int): PagedResult<ShopListResult> = result
        }
        val fakeUsecase = object : FindShopsUsecase(fakeQueryService) {
            override fun execute(page: Int, size: Int): PagedResult<ShopListResult> = result
        }
        return ShopController(fakeUsecase)
    }

    @Nested
    inner class 店舗一覧取得 {
        @Test
        fun `正常に店舗一覧を取得すると200が返る`() {
            val controller = createController()

            val response = controller.listShops(page = 0, size = 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `レスポンスボディの店舗フィールドが正しい`() {
            val controller = createController()

            val response = controller.listShops(page = 0, size = 20)

            val item = response.body?.items?.get(0)
            assertEquals("00000000-0000-4000-8000-000000000031", item?.id)
            assertEquals("珈琲工房 まめとしょ", item?.name)
            assertEquals("東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。", item?.introduction)
            assertEquals("https://mametosho.example.com", item?.shopUrl)
            assertEquals("TOKYO", item?.prefecture)
            assertEquals("https://placehold.jp/100x100.png", item?.logoImageUrl)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val emptyResult = PagedResult<ShopListResult>(
                items = emptyList(),
                totalCount = 0L,
                page = 0,
                size = 20,
            )
            val controller = createController(result = emptyResult)

            val response = controller.listShops(page = 0, size = 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }

        @Test
        fun `ページネーションパラメータが正しく渡される`() {
            val controller = createController()

            val response = controller.listShops(page = 1, size = 10)

            assertEquals(HttpStatus.OK, response.statusCode)
        }
    }
}
