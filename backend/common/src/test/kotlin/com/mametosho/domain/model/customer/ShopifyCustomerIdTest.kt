package com.mametosho.domain.model.customer

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopifyCustomerIdTest {

    @Nested
    inner class 生成テスト {
        @Test
        fun `正常に生成できる`() {
            val id = ShopifyCustomerId("gid://shopify/Customer/100001")
            assertEquals("gid://shopify/Customer/100001", id.value)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `空文字の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ShopifyCustomerId("")
            }
        }

        @Test
        fun `256文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ShopifyCustomerId("a".repeat(256))
            }
        }
    }
}
