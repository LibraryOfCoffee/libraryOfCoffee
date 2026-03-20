package com.mametosho.domain.model.administrator

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class EmailTest {

    @Nested
    inner class 生成テスト {
        @Test
        fun `正常なメールアドレスで生成できる`() {
            val email = Email("admin@mametosho.com")
            assertEquals("admin@mametosho.com", email.value)
        }
    }

    @Nested
    inner class バリデーション {
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
}
