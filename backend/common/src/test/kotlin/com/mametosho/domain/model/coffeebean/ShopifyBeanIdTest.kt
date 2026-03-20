package com.mametosho.domain.model.coffeebean

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopifyBeanIdTest {

    @Test
    fun `正常に生成できる`() {
        val id = ShopifyBeanId("gid://shopify/Product/400001")
        assertEquals("gid://shopify/Product/400001", id.value)
    }

    @Test
    fun `空文字の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            ShopifyBeanId("")
        }
    }

    @Test
    fun `256文字以上の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            ShopifyBeanId("a".repeat(256))
        }
    }
}
