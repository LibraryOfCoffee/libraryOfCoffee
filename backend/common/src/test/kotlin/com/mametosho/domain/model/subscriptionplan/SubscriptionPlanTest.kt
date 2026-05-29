package com.mametosho.domain.model.subscriptionplan

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class SubscriptionPlanTest {

    private fun createSubscriptionPlan(
        label: String = "定番",
        gramWeight: Int = 60,
        beanQuantity: Int = 4,
        subscriptionPrice: Int = 3800,
        singlePrice: Int = 4200,
        isRecommended: Boolean = true,
    ): SubscriptionPlan = SubscriptionPlan(
        id = SubscriptionPlanId("00000000-0000-4000-8000-00000000000d"),
        shopifySubscriptionId = ShopifySubscriptionId("shopify-sub-1"),
        label = label,
        gramWeight = gramWeight,
        beanQuantity = beanQuantity,
        subscriptionPrice = subscriptionPrice,
        singlePrice = singlePrice,
        isRecommended = isRecommended,
    )

    @Nested
    inner class 生成テスト {

        @Test
        fun `正常にSubscriptionPlanを生成できる`() {
            val plan = createSubscriptionPlan()
            assertEquals("定番", plan.label)
            assertEquals(60, plan.gramWeight)
            assertEquals(4, plan.beanQuantity)
            assertEquals(3800, plan.subscriptionPrice)
            assertEquals(4200, plan.singlePrice)
            assertEquals(true, plan.isRecommended)
        }
    }

    @Nested
    inner class バリデーション {

        @Test
        fun `subscriptionPriceが0の場合は生成できる`() {
            val plan = createSubscriptionPlan(subscriptionPrice = 0)
            assertEquals(0, plan.subscriptionPrice)
        }

        @Test
        fun `subscriptionPriceが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createSubscriptionPlan(subscriptionPrice = -1)
            }
        }

        @Test
        fun `singlePriceが0の場合は生成できる`() {
            val plan = createSubscriptionPlan(singlePrice = 0)
            assertEquals(0, plan.singlePrice)
        }

        @Test
        fun `singlePriceが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createSubscriptionPlan(singlePrice = -1)
            }
        }

        @Test
        fun `gramWeightが1の場合は生成できる`() {
            val plan = createSubscriptionPlan(gramWeight = 1)
            assertEquals(1, plan.gramWeight)
        }

        @Test
        fun `gramWeightが0の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createSubscriptionPlan(gramWeight = 0)
            }
        }

        @Test
        fun `gramWeightが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createSubscriptionPlan(gramWeight = -1)
            }
        }

        @Test
        fun `beanQuantityが1の場合は生成できる`() {
            val plan = createSubscriptionPlan(beanQuantity = 1)
            assertEquals(1, plan.beanQuantity)
        }

        @Test
        fun `beanQuantityが0の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createSubscriptionPlan(beanQuantity = 0)
            }
        }

        @Test
        fun `beanQuantityが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createSubscriptionPlan(beanQuantity = -1)
            }
        }
    }
}
