package com.mametosho.cs.infrastructure.persistence.query

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.ShopListRow
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.ShopQueryMapper
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopQueryServiceImplTest {

    private var capturedSize: Int? = null
    private var capturedOffset: Int? = null

    private val sampleRow = ShopListRow(
        id = "00000000-0000-4000-8000-000000000031",
        name = "珈琲工房 まめとしょ",
        introduction = "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
        shopUrl = "https://mametosho.example.com",
        prefecture = "TOKYO",
        logoImageUrl = "https://placehold.jp/100x100.png",
    )

    private fun createService(
        rows: List<ShopListRow> = listOf(sampleRow),
        count: Long = 1L,
    ): ShopQueryServiceImpl {
        val fakeMapper = object : ShopQueryMapper {
            override fun findListRows(size: Int, offset: Int): List<ShopListRow> {
                capturedSize = size
                capturedOffset = offset
                return rows
            }

            override fun count(): Long = count
        }
        return ShopQueryServiceImpl(fakeMapper)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `店舗一覧を取得できる`() {
            val service = createService()

            val result = service.findList(page = 0, size = 20)

            assertEquals(1, result.items.size)
            assertEquals(1L, result.totalCount)
            assertEquals(0, result.page)
            assertEquals(20, result.size)
        }

        @Test
        fun `Rowが正しくResultに変換される`() {
            val service = createService()

            val result = service.findList(page = 0, size = 20)

            val item = result.items[0]
            assertEquals("00000000-0000-4000-8000-000000000031", item.id)
            assertEquals("珈琲工房 まめとしょ", item.name)
            assertEquals("東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。", item.introduction)
            assertEquals("https://mametosho.example.com", item.shopUrl)
            assertEquals("TOKYO", item.prefecture)
            assertEquals("https://placehold.jp/100x100.png", item.logoImageUrl)
        }

        @Test
        fun `offsetがpage×sizeで計算される`() {
            val service = createService()

            service.findList(page = 2, size = 10)

            assertEquals(10, capturedSize)
            assertEquals(20, capturedOffset)
        }

        @Test
        fun `件数が0件の場合は空リストを返す`() {
            val service = createService(rows = emptyList(), count = 0L)

            val result = service.findList(page = 0, size = 20)

            assertEquals(0, result.items.size)
            assertEquals(0L, result.totalCount)
        }
    }
}
