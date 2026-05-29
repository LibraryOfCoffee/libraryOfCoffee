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
        description = "テスト用の説明文です。",
        imageUrl = "https://example.com/images/test.jpg",
        shopName = "テスト珈琲焙煎所",
        tasteProfiles = listOf(
            CoffeeBeanListResult.TasteProfileResult(name = "酸味", value = 60),
            CoffeeBeanListResult.TasteProfileResult(name = "苦味", value = 20),
        ),
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
            assertEquals("テスト用の説明文です。", response.description)
            assertEquals("https://example.com/images/test.jpg", response.imageUrl)
            assertEquals("テスト珈琲焙煎所", response.shopName)
            assertEquals(2, response.tasteProfiles.size)
            assertEquals("酸味", response.tasteProfiles[0].name)
            assertEquals(60, response.tasteProfiles[0].value)
            assertEquals("苦味", response.tasteProfiles[1].name)
            assertEquals(20, response.tasteProfiles[1].value)
        }

        @Test
        fun `isSpecialtyがfalseの場合も正しく変換される`() {
            val response = CoffeeBeanListResponse.from(createResult(isSpecialty = false))

            assertEquals(false, response.isSpecialty)
        }
    }
}
