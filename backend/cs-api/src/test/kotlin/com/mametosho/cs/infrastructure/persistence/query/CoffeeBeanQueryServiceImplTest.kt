package com.mametosho.cs.infrastructure.persistence.query

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.CoffeeBeanListRow
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.CoffeeBeanQueryMapper
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.Prefecture
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeBeanQueryServiceImplTest {

    private var capturedSize: Int? = null
    private var capturedOffset: Int? = null
    private var capturedOrigin: String? = null
    private var capturedRoastLevel: String? = null
    private var capturedPrefecture: String? = null

    private val sampleRow = CoffeeBeanListRow(
        id = "00000000-0000-4000-8000-000000000001",
        name = "テストコーヒー豆",
        origin = "エチオピア",
        roastLevel = "LIGHT",
        processingMethod = "WASHED",
        isSpecialty = true,
        description = "テスト用の説明文です。",
        imageUrl = "https://example.com/images/test.jpg",
        shopName = "テスト珈琲焙煎所",
        shopPrefecture = "TOKYO",
        tasteName = "酸味",
        evaluationValue = 60,
    )

    private fun createService(
        rows: List<CoffeeBeanListRow> = listOf(sampleRow),
        count: Long = 1L,
    ): CoffeeBeanQueryServiceImpl {
        val fakeMapper = object : CoffeeBeanQueryMapper {
            override fun findListRows(
                size: Int,
                offset: Int,
                origin: String?,
                roastLevel: String?,
                prefecture: String?,
            ): List<CoffeeBeanListRow> {
                capturedSize = size
                capturedOffset = offset
                capturedOrigin = origin
                capturedRoastLevel = roastLevel
                capturedPrefecture = prefecture
                return rows
            }

            override fun countFiltered(
                origin: String?,
                roastLevel: String?,
                prefecture: String?,
            ): Long = count
        }
        return CoffeeBeanQueryServiceImpl(fakeMapper)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `フィルタなしで一覧を取得できる`() {
            val service = createService()

            val result = service.findList(page = 0, size = 20, origin = null, roastLevel = null, prefecture = null)

            assertEquals(1, result.items.size)
            assertEquals(1L, result.totalCount)
            assertEquals(0, result.page)
            assertEquals(20, result.size)
        }

        @Test
        fun `Rowが正しくResultに変換される`() {
            val service = createService()

            val result = service.findList(page = 0, size = 20, origin = null, roastLevel = null, prefecture = null)

            val item = result.items[0]
            assertEquals("00000000-0000-4000-8000-000000000001", item.id)
            assertEquals("テストコーヒー豆", item.name)
            assertEquals("エチオピア", item.origin)
            assertEquals("LIGHT", item.roastLevel)
            assertEquals("WASHED", item.processingMethod)
            assertEquals(true, item.isSpecialty)
            assertEquals("テスト用の説明文です。", item.description)
            assertEquals("https://example.com/images/test.jpg", item.imageUrl)
            assertEquals("テスト珈琲焙煎所", item.shopName)
            assertEquals(1, item.tasteProfiles.size)
            assertEquals("酸味", item.tasteProfiles[0].name)
            assertEquals(60, item.tasteProfiles[0].value)
        }

        @Test
        fun `offsetがpage×sizeで計算される`() {
            val service = createService()

            service.findList(page = 1, size = 10, origin = null, roastLevel = null, prefecture = null)

            assertEquals(10, capturedSize)
            assertEquals(10, capturedOffset)
        }

        @Test
        fun `EnumがMapper向けに文字列名に変換されてから渡される`() {
            val service = createService()

            service.findList(
                page = 0,
                size = 20,
                origin = "エチオピア",
                roastLevel = RoastLevel.LIGHT,
                prefecture = Prefecture.TOKYO,
            )

            assertEquals("エチオピア", capturedOrigin)
            assertEquals("LIGHT", capturedRoastLevel)
            assertEquals("TOKYO", capturedPrefecture)
        }

        @Test
        fun `件数が0件の場合は空リストを返す`() {
            val service = createService(rows = emptyList(), count = 0L)

            val result = service.findList(page = 0, size = 20, origin = null, roastLevel = null, prefecture = null)

            assertEquals(0, result.items.size)
            assertEquals(0L, result.totalCount)
        }

        @Test
        fun `複数テイストを持つ豆は1つのResultにグルーピングされる`() {
            val rows = listOf(
                sampleRow.copy(tasteName = "酸味", evaluationValue = 60),
                sampleRow.copy(tasteName = "苦味", evaluationValue = 20),
            )
            val service = createService(rows = rows, count = 1L)

            val result = service.findList(page = 0, size = 20, origin = null, roastLevel = null, prefecture = null)

            assertEquals(1, result.items.size)
            assertEquals(2, result.items[0].tasteProfiles.size)
        }
    }
}
