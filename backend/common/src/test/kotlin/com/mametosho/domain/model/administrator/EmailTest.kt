package com.mametosho.domain.model.administrator

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class EmailTest {

    @Test
    fun `正常なメールアドレスで生成できる`() {
        val email = Email("admin@mametosho.com")
        assertEquals("admin@mametosho.com", email.value)
    }

    @Test
    fun `空文字の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            Email("")
        }
    }

    @Test
    fun `256文字以上の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            Email("a".repeat(250) + "@b.com")
        }
    }

    @Test
    fun `アットマークがない場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            Email("invalid-email")
        }
    }
}
