package com.mametosho.domain.model.customer

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomerIdTest {

    @Test
    fun `正常なUUID形式で生成できる`() {
        val id = CustomerId("00000000-0000-4000-8000-000000000001")
        assertEquals("00000000-0000-4000-8000-000000000001", id.value)
    }

    @Test
    fun `空文字の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            CustomerId("")
        }
    }

    @Test
    fun `不正なUUID形式の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            CustomerId("invalid-uuid")
        }
    }
}
