package com.mametosho.domain.model.taste

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class TasteTest {

    @Nested
    inner class 生成テスト {

        @Test
        fun `正常にTasteを生成できる`() {
            val taste = Taste(
                id = TasteId("00000000-0000-4000-8000-000000000004"),
                name = "酸味",
            )
            assertEquals("酸味", taste.name)
        }
    }

    @Nested
    inner class バリデーション {

        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                Taste(
                    id = TasteId("00000000-0000-4000-8000-000000000004"),
                    name = "",
                )
            }
        }

        @Test
        fun `nameがスペースのみの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                Taste(
                    id = TasteId("00000000-0000-4000-8000-000000000004"),
                    name = "   ",
                )
            }
        }

        @Test
        fun `nameが256文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                Taste(
                    id = TasteId("00000000-0000-4000-8000-000000000004"),
                    name = "a".repeat(256),
                )
            }
        }
    }
}
