package com.mametosho.domain.model.subscriptionplan

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class SubscriptionPlanTest {

    private fun createSubscriptionPlan(
        price: Int = 3000,
        beanQuantity: Int = 3,
    ): SubscriptionPlan = SubscriptionPlan(
        id = SubscriptionPlanId("00000000-0000-4000-8000-00000000000d"),
        shopifySubscriptionId = ShopifySubscriptionId("shopify-sub-1"),
        price = price,
        beanQuantity = beanQuantity,
    )

    @Test
    fun `正常にSubscriptionPlanを生成できる`() {
        val plan = createSubscriptionPlan()
        assertEquals(3000, plan.price)
        assertEquals(3, plan.beanQuantity)
    }

    @Test
    fun `priceが0の場合は生成できる`() {
        val plan = createSubscriptionPlan(price = 0)
        assertEquals(0, plan.price)
    }

    @Test
    fun `priceが負の値の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createSubscriptionPlan(price = -1)
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
