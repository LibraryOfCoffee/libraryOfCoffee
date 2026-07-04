package com.mametosho.admin.presentation.dto.response

import com.mametosho.admin.application.query.result.CoffeeBeanDetailResult
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CoffeeBeanDetailResponseTest {

    private fun createResult(
        farm: String? = "テスト農園",
        images: List<CoffeeBeanDetailResult.ImageResult> = emptyList(),
        tastes: List<CoffeeBeanDetailResult.TasteResult> = emptyList(),
    ): CoffeeBeanDetailResult = CoffeeBeanDetailResult(
        id = "00000000-0000-4000-8000-000000000001",
        shopId = "00000000-0000-4000-8000-000000000002",
        shopName = "テスト店舗",
        shopifyBeanId = "test-bean-001",
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = farm,
        roastLevel = "MEDIUM",
        processingMethod = "WASHED",
        isSpecialty = true,
        publishStatus = "PUBLISHED",
        images = images,
        tastes = tastes,
    )

    @Nested
    inner class 正常系変換 {
        @Test
        fun `全フィールドが正しく変換される`() {
            val image = CoffeeBeanDetailResult.ImageResult(
                id = "00000000-0000-4000-8000-000000000010",
                type = "MAIN",
                imageUrl = "https://example.com/image.jpg",
            )
            val taste = CoffeeBeanDetailResult.TasteResult(
                id = "00000000-0000-4000-8000-000000000020",
                tasteId = "00000000-0000-4000-8000-000000000041",
                tasteName = "酸味",
                evaluationValue = 4,
            )
            val result = createResult(images = listOf(image), tastes = listOf(taste))

            val response = CoffeeBeanDetailResponse.from(result)

            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
            assertEquals("00000000-0000-4000-8000-000000000002", response.shopId)
            assertEquals("テスト店舗", response.shopName)
            assertEquals("test-bean-001", response.shopifyBeanId)
            assertEquals("テストコーヒー豆", response.name)
            assertEquals("テスト説明文", response.description)
            assertEquals("エチオピア", response.origin)
            assertEquals("テスト農園", response.farm)
            assertEquals("MEDIUM", response.roastLevel)
            assertEquals("WASHED", response.processingMethod)
            assertEquals(true, response.isSpecialty)
            assertEquals("PUBLISHED", response.publishStatus)
            assertEquals(1, response.images.size)
            assertEquals("00000000-0000-4000-8000-000000000010", response.images[0].id)
            assertEquals("MAIN", response.images[0].type)
            assertEquals("https://example.com/image.jpg", response.images[0].imageUrl)
            assertEquals(1, response.tastes.size)
            assertEquals("00000000-0000-4000-8000-000000000020", response.tastes[0].id)
            assertEquals("00000000-0000-4000-8000-000000000041", response.tastes[0].tasteId)
            assertEquals("酸味", response.tastes[0].tasteName)
            assertEquals(4, response.tastes[0].evaluationValue)
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `farmがnullの場合nullが返る`() {
            val result = createResult(farm = null)

            val response = CoffeeBeanDetailResponse.from(result)

            assertNull(response.farm)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像が空の場合空リストが返る`() {
            val result = createResult(images = emptyList())

            val response = CoffeeBeanDetailResponse.from(result)

            assertEquals(emptyList(), response.images)
        }

        @Test
        fun `テイスト評価が空の場合空リストが返る`() {
            val result = createResult(tastes = emptyList())

            val response = CoffeeBeanDetailResponse.from(result)

            assertEquals(emptyList(), response.tastes)
        }
    }
}
