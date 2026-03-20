package com.mametosho.domain.model.shop

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopifyShopIdTest {

    @Nested
    inner class 生成テスト {
        @Test
        fun `正常に生成できる`() {
            val id = ShopifyShopId("gid://shopify/Shop/300001")
            assertEquals("gid://shopify/Shop/300001", id.value)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `空文字の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ShopifyShopId("")
            }
        }

        @Test
        fun `256文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ShopifyShopId("a".repeat(256))
            }
        }
    }
}
