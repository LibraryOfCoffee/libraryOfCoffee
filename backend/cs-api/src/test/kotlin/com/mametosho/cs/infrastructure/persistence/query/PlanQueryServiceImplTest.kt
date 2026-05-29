package com.mametosho.cs.infrastructure.persistence.query

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.PlanListRow
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.PlanQueryMapper
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanQueryServiceImplTest {

    private val sampleRow = PlanListRow(
        id = "00000000-0000-4000-8000-000000000024",
        label = "定番",
        gramWeight = 30,
        beanQuantity = 4,
        price = 1950,
        type = "SUBSCRIPTION",
        isRecommended = true,
    )

    private fun createService(
        rows: List<PlanListRow> = listOf(sampleRow),
    ): PlanQueryServiceImpl {
        val fakeMapper = object : PlanQueryMapper {
            override fun findListRows(): List<PlanListRow> = rows
        }
        return PlanQueryServiceImpl(fakeMapper)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `プラン一覧を取得できる`() {
            val service = createService()

            val result = service.findList()

            assertEquals(1, result.size)
        }

        @Test
        fun `Rowが正しくResultに変換される`() {
            val service = createService()

            val result = service.findList()

            val item = result[0]
            assertEquals("00000000-0000-4000-8000-000000000024", item.id)
            assertEquals("定番", item.label)
            assertEquals(30, item.gramWeight)
            assertEquals(4, item.beanQuantity)
            assertEquals(1950, item.price)
            assertEquals("SUBSCRIPTION", item.type)
            assertEquals(true, item.isRecommended)
        }

        @Test
        fun `件数が0件の場合は空リストを返す`() {
            val service = createService(rows = emptyList())

            val result = service.findList()

            assertEquals(0, result.size)
        }
    }
}
