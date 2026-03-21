package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImage
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageType
import com.mametosho.domain.model.coffeebean.CoffeeBeanTaste
import com.mametosho.domain.model.coffeebean.CoffeeBeanTasteId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CoffeeBeanDetailResponseTest {

    private fun createCoffeeBean(
        farm: String? = "テスト農園",
        images: List<CoffeeBeanImage> = emptyList(),
        tastes: List<CoffeeBeanTaste> = emptyList(),
    ): CoffeeBean = CoffeeBean(
        id = CoffeeBeanId("00000000-0000-4000-8000-000000000001"),
        shopId = ShopId("00000000-0000-4000-8000-000000000002"),
        shopifyBeanId = ShopifyBeanId("test-bean-001"),
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = farm,
        roastLevel = RoastLevel.MEDIUM,
        processingMethod = ProcessingMethod.WASHED,
        isSpecialty = true,
        images = images,
        tastes = tastes,
    )

    @Nested
    inner class 正常系変換 {
        @Test
        fun `全フィールドが正しく変換される`() {
            val image = CoffeeBeanImage(
                id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000010"),
                type = CoffeeBeanImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/image.jpg"),
            )
            val taste = CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000020"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000030"),
                evaluationValue = 4,
            )
            val coffeeBean = createCoffeeBean(images = listOf(image), tastes = listOf(taste))

            val response = CoffeeBeanDetailResponse.from(coffeeBean)

            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
            assertEquals("00000000-0000-4000-8000-000000000002", response.shopId)
            assertEquals("test-bean-001", response.shopifyBeanId)
            assertEquals("テストコーヒー豆", response.name)
            assertEquals("テスト説明文", response.description)
            assertEquals("エチオピア", response.origin)
            assertEquals("テスト農園", response.farm)
            assertEquals("MEDIUM", response.roastLevel)
            assertEquals("WASHED", response.processingMethod)
            assertEquals(true, response.isSpecialty)
            assertEquals(1, response.images.size)
            assertEquals("00000000-0000-4000-8000-000000000010", response.images[0].id)
            assertEquals("MAIN", response.images[0].type)
            assertEquals("https://example.com/image.jpg", response.images[0].imageUrl)
            assertEquals(1, response.tastes.size)
            assertEquals("00000000-0000-4000-8000-000000000020", response.tastes[0].id)
            assertEquals("00000000-0000-4000-8000-000000000030", response.tastes[0].tasteId)
            assertEquals(4, response.tastes[0].evaluationValue)
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `farmがnullの場合nullが返る`() {
            val coffeeBean = createCoffeeBean(farm = null)

            val response = CoffeeBeanDetailResponse.from(coffeeBean)

            assertNull(response.farm)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像が空の場合空リストが返る`() {
            val coffeeBean = createCoffeeBean(images = emptyList())

            val response = CoffeeBeanDetailResponse.from(coffeeBean)

            assertEquals(emptyList(), response.images)
        }

        @Test
        fun `テイスト評価が空の場合空リストが返る`() {
            val coffeeBean = createCoffeeBean(tastes = emptyList())

            val response = CoffeeBeanDetailResponse.from(coffeeBean)

            assertEquals(emptyList(), response.tastes)
        }
    }
}
