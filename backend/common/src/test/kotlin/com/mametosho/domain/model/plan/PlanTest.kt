package com.mametosho.domain.model.plan

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanTest {

    private fun createPlan(
        label: String = "定番",
        gramWeight: Int = 60,
        beanQuantity: Int = 4,
        price: Int = 3800,
        type: PlanType = PlanType.SUBSCRIPTION,
        isRecommended: Boolean = true,
    ): Plan = Plan(
        id = PlanId("00000000-0000-4000-8000-00000000000d"),
        shopifyPlanId = ShopifyPlanId("shopify-plan-1"),
        label = label,
        gramWeight = gramWeight,
        beanQuantity = beanQuantity,
        price = price,
        type = type,
        isRecommended = isRecommended,
    )

    @Nested
    inner class 生成テスト {

        @Test
        fun `正常にPlanを生成できる`() {
            val plan = createPlan()
            assertEquals("定番", plan.label)
            assertEquals(60, plan.gramWeight)
            assertEquals(4, plan.beanQuantity)
            assertEquals(3800, plan.price)
            assertEquals(PlanType.SUBSCRIPTION, plan.type)
            assertEquals(true, plan.isRecommended)
        }

        @Test
        fun `SINGLEタイプのPlanを生成できる`() {
            val plan = createPlan(type = PlanType.SINGLE, price = 4200)
            assertEquals(PlanType.SINGLE, plan.type)
            assertEquals(4200, plan.price)
        }
    }

    @Nested
    inner class バリデーション {

        @Test
        fun `priceが0の場合は生成できる`() {
            val plan = createPlan(price = 0)
            assertEquals(0, plan.price)
        }

        @Test
        fun `priceが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createPlan(price = -1)
            }
        }

        @Test
        fun `gramWeightが1の場合は生成できる`() {
            val plan = createPlan(gramWeight = 1)
            assertEquals(1, plan.gramWeight)
        }

        @Test
        fun `gramWeightが0の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createPlan(gramWeight = 0)
            }
        }

        @Test
        fun `gramWeightが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createPlan(gramWeight = -1)
            }
        }

        @Test
        fun `beanQuantityが1の場合は生成できる`() {
            val plan = createPlan(beanQuantity = 1)
            assertEquals(1, plan.beanQuantity)
        }

        @Test
        fun `beanQuantityが0の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createPlan(beanQuantity = 0)
            }
        }

        @Test
        fun `beanQuantityが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createPlan(beanQuantity = -1)
            }
        }
    }
}
