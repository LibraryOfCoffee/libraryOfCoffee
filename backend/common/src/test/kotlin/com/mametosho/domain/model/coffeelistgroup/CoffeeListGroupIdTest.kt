package com.mametosho.domain.model.coffeelistgroup

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeListGroupIdTest {

    @Nested
    inner class 生成テスト {
        @Test
        fun `正常なUUID形式で生成できる`() {
            val id = CoffeeListGroupId("00000000-0000-4000-8000-000000000001")
            assertEquals("00000000-0000-4000-8000-000000000001", id.value)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `空文字の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                CoffeeListGroupId("")
            }
        }

        @Test
        fun `不正なUUID形式の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                CoffeeListGroupId("invalid-uuid")
            }
        }
    }
}
