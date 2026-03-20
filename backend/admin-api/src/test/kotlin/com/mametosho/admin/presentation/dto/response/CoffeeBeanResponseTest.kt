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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CoffeeBeanResponseTest {

    private fun createCoffeeBean(
        farm: String? = "テスト農園",
        images: List<CoffeeBeanImage> = listOf(
            CoffeeBeanImage(
                id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000011"),
                type = CoffeeBeanImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/bean.png"),
            ),
        ),
        tastes: List<CoffeeBeanTaste> = listOf(
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000021"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000031"),
                evaluationValue = 3,
            ),
        ),
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

    @Test
    fun `正常にCoffeeBeanからCoffeeBeanResponseに変換できる`() {
        val response = CoffeeBeanResponse.from(createCoffeeBean())

        assertEquals("00000000-0000-4000-8000-000000000001", response.id)
        assertEquals("00000000-0000-4000-8000-000000000002", response.shopId)
        assertEquals("test-bean-001", response.shopifyBeanId)
        assertEquals("テストコーヒー豆", response.name)
        assertEquals("テスト説明文", response.description)
        assertEquals("エチオピア", response.origin)
        assertEquals("テスト農園", response.farm)
        assertEquals("MEDIUM", response.roastLevel)
        assertEquals("WASHED", response.processingMethod)
        assertTrue(response.isSpecialty)
        assertEquals(1, response.images.size)
        assertEquals("00000000-0000-4000-8000-000000000011", response.images[0].id)
        assertEquals("MAIN", response.images[0].type)
        assertEquals("https://example.com/bean.png", response.images[0].imageUrl)
        assertEquals(1, response.tastes.size)
        assertEquals("00000000-0000-4000-8000-000000000021", response.tastes[0].id)
        assertEquals("00000000-0000-4000-8000-000000000031", response.tastes[0].tasteId)
        assertEquals(3, response.tastes[0].evaluationValue)
    }

    @Test
    fun `farmがnullの場合もレスポンスに変換できる`() {
        val response = CoffeeBeanResponse.from(createCoffeeBean(farm = null))
        assertNull(response.farm)
    }

    @Test
    fun `画像が空の場合もレスポンスに変換できる`() {
        val response = CoffeeBeanResponse.from(createCoffeeBean(images = emptyList()))
        assertEquals(0, response.images.size)
    }

    @Test
    fun `テイストが空の場合もレスポンスに変換できる`() {
        val response = CoffeeBeanResponse.from(createCoffeeBean(tastes = emptyList()))
        assertEquals(0, response.tastes.size)
    }
}
