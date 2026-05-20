package com.mametosho.cs.presentation.dto.response

import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeBeanListResponseTest {

    private fun createResult(
        isSpecialty: Boolean = true,
    ): CoffeeBeanListResult = CoffeeBeanListResult(
        id = "00000000-0000-4000-8000-000000000001",
        name = "テストコーヒー豆",
        origin = "エチオピア",
        roastLevel = "LIGHT",
        processingMethod = "WASHED",
        isSpecialty = isSpecialty,
    )

    @Nested
    inner class 正常系変換 {
        @Test
        fun `CoffeeBeanListResultからCoffeeBeanListResponseに正しく変換される`() {
            val response = CoffeeBeanListResponse.from(createResult())

            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
            assertEquals("テストコーヒー豆", response.name)
            assertEquals("エチオピア", response.origin)
            assertEquals("LIGHT", response.roastLevel)
            assertEquals("WASHED", response.processingMethod)
            assertEquals(true, response.isSpecialty)
        }

        @Test
        fun `isSpecialtyがfalseの場合も正しく変換される`() {
            val response = CoffeeBeanListResponse.from(createResult(isSpecialty = false))

            assertEquals(false, response.isSpecialty)
        }
    }
}
