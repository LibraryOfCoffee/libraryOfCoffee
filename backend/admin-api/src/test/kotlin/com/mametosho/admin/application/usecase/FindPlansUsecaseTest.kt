package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.model.plan.PlanId
import com.mametosho.domain.model.plan.PlanType
import com.mametosho.domain.model.plan.ShopifyPlanId
import com.mametosho.domain.repository.PlanRepository
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class FindPlansUsecaseTest {

    private val plan = Plan(
        id = PlanId("00000000-0000-4000-8000-000000000024"),
        shopifyPlanId = ShopifyPlanId("existing-plan-001"),
        label = "定番",
        gramWeight = 60,
        beanQuantity = 4,
        price = 3800,
        type = PlanType.SUBSCRIPTION,
        isRecommended = true,
    )

    private var lastArgs: Triple<Int, Int, String?>? = null

    private val fakeRepository = object : PlanRepository {
        override fun findAll(): List<Plan> = listOf(plan)

        override fun findAll(page: Int, size: Int, keyword: String?): Pair<List<Plan>, Long> {
            lastArgs = Triple(page, size, keyword)
            return Pair(listOf(plan), 1L)
        }

        override fun findById(id: PlanId): Plan? = null

        override fun save(plan: Plan) = Unit
    }

    private val usecase = FindPlansUsecase(fakeRepository)

    @Nested
    inner class 正常系 {
        @Test
        fun `ページネーション付きでPlan一覧が返る`() {
            val result = usecase.execute(0, 20, null)

            assertEquals(1, result.items.size)
            assertEquals(1L, result.totalCount)
            assertEquals(0, result.page)
            assertEquals(20, result.size)
        }

        @Test
        fun `検索キーワードがリポジトリに渡される`() {
            usecase.execute(1, 10, "定番")

            assertEquals(Triple(1, 10, "定番"), lastArgs)
        }
    }
}
