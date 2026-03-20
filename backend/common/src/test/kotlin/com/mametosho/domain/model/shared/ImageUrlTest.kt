package com.mametosho.domain.model.shared

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageUrlTest {

    @Nested
    inner class 生成テスト {
        @Test
        fun `正常なURLで生成できる`() {
            val url = ImageUrl("https://example.com/image.jpg")
            assertEquals("https://example.com/image.jpg", url.value)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `空文字の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ImageUrl("")
            }
        }

        @Test
        fun `2049文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                ImageUrl("a".repeat(2049))
            }
        }
    }
}
