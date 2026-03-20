package com.mametosho.domain.model.subscriptionplan

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopifySubscriptionIdTest {

    @Nested
    inner class 生成テスト {
        @Test
        fun `正常に生成できる`() {
            val id = ShopifySubscriptionId("gid://shopify/SellingPlan/200001")
            assertEquals("gid://shopify/SellingPlan/200001", id.value)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `空文字の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ShopifySubscriptionId("")
            }
        }

        @Test
        fun `256文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ShopifySubscriptionId("a".repeat(256))
            }
        }
    }
}
