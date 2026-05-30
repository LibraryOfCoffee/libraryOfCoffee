package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.query.CoffeeBeanQueryService
import com.mametosho.admin.application.query.result.CoffeeBeanDetailResult
import com.mametosho.admin.application.query.result.CoffeeBeanListResult
import com.mametosho.admin.application.result.PagedResult
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetCoffeeBeanUsecaseTest {

    private val existingResult = CoffeeBeanDetailResult(
        id = "00000000-0000-4000-8000-000000000001",
        shopId = "00000000-0000-4000-8000-000000000002",
        shopifyBeanId = "test-bean-001",
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = "テスト農園",
        roastLevel = "MEDIUM",
        processingMethod = "WASHED",
        isSpecialty = true,
        images = emptyList(),
        tastes = emptyList(),
    )

    private fun createUsecase(result: CoffeeBeanDetailResult? = existingResult): GetCoffeeBeanUsecase {
        val fakeQueryService = object : CoffeeBeanQueryService {
            override fun findList(page: Int, size: Int): PagedResult<CoffeeBeanListResult> =
                PagedResult(emptyList(), 0L, page, size)
            override fun findDetail(id: String): CoffeeBeanDetailResult? =
                if (id == existingResult.id) result else null
        }
        return GetCoffeeBeanUsecase(fakeQueryService)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `存在するIDでコーヒー豆を取得できる`() {
            val usecase = createUsecase()
            val result = usecase.execute("00000000-0000-4000-8000-000000000001")

            assertEquals(existingResult, result)
        }
    }

    @Nested
    inner class 存在しない場合 {
        @Test
        fun `存在しないIDの場合nullが返る`() {
            val usecase = createUsecase()
            val result = usecase.execute("00000000-0000-4000-8000-999999999999")

            assertNull(result)
        }
    }
}
