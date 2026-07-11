package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.UpdatePlanRequest
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

class UpdatePlanUsecaseTest {

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

    private val savedPlans = mutableListOf<Plan>()

    private val fakeRepository = object : PlanRepository {
        override fun findAll(): List<Plan> = listOf(existingPlan)

        override fun findAll(page: Int, size: Int, keyword: String?): Pair<List<Plan>, Long> =
            Pair(listOf(existingPlan), 1L)

        override fun findById(id: PlanId): Plan? =
            if (id.value == existingPlanId) existingPlan else null

        override fun save(plan: Plan) {
            savedPlans.add(plan)
        }
    }

    private val usecase = UpdatePlanUsecase(fakeRepository)

    private fun createRequest(
        shopifyPlanId: String = "updated-plan-001",
        label: String = "プレミアム",
        gramWeight: Int = 90,
        beanQuantity: Int = 5,
        price: Int = 5800,
        type: String = "SINGLE",
        isRecommended: Boolean = false,
    ): UpdatePlanRequest = UpdatePlanRequest(
        shopifyPlanId = shopifyPlanId,
        label = label,
        gramWeight = gramWeight,
        beanQuantity = beanQuantity,
        price = price,
        type = type,
        isRecommended = isRecommended,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にPlanを編集できる`() {
            val plan = usecase.execute(existingPlanId, createRequest())

            assertNotNull(plan)
            assertEquals("updated-plan-001", plan.shopifyPlanId.value)
            assertEquals("プレミアム", plan.label)
            assertEquals(90, plan.gramWeight)
            assertEquals(5, plan.beanQuantity)
            assertEquals(5800, plan.price)
            assertEquals(PlanType.SINGLE, plan.type)
            assertEquals(false, plan.isRecommended)
        }

        @Test
        fun `編集後もPlanIdが変わらない`() {
            val plan = usecase.execute(existingPlanId, createRequest())

            assertNotNull(plan)
            assertEquals(existingPlanId, plan.id.value)
        }
    }

    @Nested
    inner class 存在しないID {
        @Test
        fun `存在しないIDの場合はnullが返る`() {
            val plan = usecase.execute("00000000-0000-4000-8000-999999999999", createRequest())

            assertNull(plan)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `編集したPlanがリポジトリに保存される`() {
            savedPlans.clear()
            usecase.execute(existingPlanId, createRequest())

            assertEquals(1, savedPlans.size)
            assertEquals("プレミアム", savedPlans[0].label)
        }

        @Test
        fun `存在しないIDの場合はリポジトリに保存されない`() {
            savedPlans.clear()
            usecase.execute("00000000-0000-4000-8000-999999999999", createRequest())

            assertEquals(0, savedPlans.size)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `gramWeightが不正な場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingPlanId, createRequest(gramWeight = 100))
            }
        }

        @Test
        fun `typeが不正な場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingPlanId, createRequest(type = "INVALID"))
            }
        }

        @Test
        fun `不正なUUID形式のIDの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute("invalid-id", createRequest())
            }
        }
    }
}
