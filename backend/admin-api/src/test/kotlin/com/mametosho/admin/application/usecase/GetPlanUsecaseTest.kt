package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.model.plan.PlanId
import com.mametosho.domain.model.plan.PlanType
import com.mametosho.domain.model.plan.ShopifyPlanId
import com.mametosho.domain.repository.PlanRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GetPlanUsecaseTest {

    private val existingPlanId = "00000000-0000-4000-8000-000000000024"

    private val existingPlan = Plan(
        id = PlanId(existingPlanId),
        shopifyPlanId = ShopifyPlanId("existing-plan-001"),
        label = "定番",
        gramWeight = 60,
        beanQuantity = 4,
        price = 3800,
        type = PlanType.SUBSCRIPTION,
        isRecommended = true,
    )

    private val fakeRepository = object : PlanRepository {
        override fun findAll(): List<Plan> = listOf(existingPlan)

        override fun findAll(page: Int, size: Int, keyword: String?): Pair<List<Plan>, Long> =
            Pair(listOf(existingPlan), 1L)

        override fun findById(id: PlanId): Plan? =
            if (id.value == existingPlanId) existingPlan else null

        override fun save(plan: Plan) = Unit
    }

    private val usecase = GetPlanUsecase(fakeRepository)

    @Nested
    inner class 正常系 {
        @Test
        fun `存在するIDの場合はPlanが返る`() {
            val plan = usecase.execute(existingPlanId)

            assertNotNull(plan)
            assertEquals(existingPlanId, plan.id.value)
            assertEquals("定番", plan.label)
        }
    }

    @Nested
    inner class 存在しないID {
        @Test
        fun `存在しないIDの場合はnullが返る`() {
            val plan = usecase.execute("00000000-0000-4000-8000-999999999999")

            assertNull(plan)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `不正なUUID形式のIDの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute("invalid-id")
            }
        }
    }
}
