package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeBeanTest {

    private fun createCoffeeBean(
        tastes: List<CoffeeBeanTaste> = emptyList(),
        images: List<CoffeeBeanImage> = emptyList(),
    ): CoffeeBean = CoffeeBean(
        id = CoffeeBeanId("00000000-0000-4000-8000-000000000001"),
        shopId = ShopId("00000000-0000-4000-8000-000000000003"),
        shopifyBeanId = ShopifyBeanId("shopify-bean-1"),
        name = "エチオピア イルガチェフェ",
        description = "フルーティーな香りが特徴",
        origin = "エチオピア",
        farm = "イルガチェフェ農園",
        roastLevel = RoastLevel.LIGHT,
        processingMethod = ProcessingMethod.WASHED,
        isSpecialty = false,
        images = images,
        tastes = tastes,
    )

    @Test
    fun `正常にCoffeeBeanを生成できる`() {
        val bean = createCoffeeBean()
        assertEquals("エチオピア イルガチェフェ", bean.name)
        assertEquals(RoastLevel.LIGHT, bean.roastLevel)
        assertEquals(ProcessingMethod.WASHED, bean.processingMethod)
    }

    @Test
    fun `farmがnullでも生成できる`() {
        val bean = createCoffeeBean().copy(farm = null)
        assertEquals(null, bean.farm)
    }

    @Test
    fun `異なるTasteIdのテイスト評価を複数持てる`() {
        val tastes = listOf(
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000006"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000004"),
                evaluationValue = 3,
            ),
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000007"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000005"),
                evaluationValue = 5,
            ),
        )
        val bean = createCoffeeBean(tastes = tastes)
        assertEquals(2, bean.tastes.size)
    }

    @Test
    fun `同じTasteIdのテイスト評価が重複する場合は例外が発生する`() {
        val duplicateTasteId = TasteId("00000000-0000-4000-8000-000000000004")
        val tastes = listOf(
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000006"),
                tasteId = duplicateTasteId,
                evaluationValue = 3,
            ),
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000007"),
                tasteId = duplicateTasteId,
                evaluationValue = 5,
            ),
        )
        assertThrows<IllegalArgumentException> {
            createCoffeeBean(tastes = tastes)
        }
    }

    @Test
    fun `nameが空白の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(name = "")
        }
    }

    @Test
    fun `nameが256文字以上の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(name = "a".repeat(256))
        }
    }

    @Test
    fun `descriptionが空白の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(description = "")
        }
    }

    @Test
    fun `descriptionが10001文字以上の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(description = "a".repeat(10001))
        }
    }

    @Test
    fun `originが空白の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(origin = "")
        }
    }

    @Test
    fun `originが256文字以上の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(origin = "a".repeat(256))
        }
    }

    @Test
    fun `farmが空白の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(farm = "")
        }
    }

    @Test
    fun `farmが256文字以上の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            createCoffeeBean().copy(farm = "a".repeat(256))
        }
    }

    @Test
    fun `画像を持つCoffeeBeanを生成できる`() {
        val images = listOf(
            CoffeeBeanImage(
                id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000009"),
                type = CoffeeBeanImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/bean.jpg"),
            ),
        )
        val bean = createCoffeeBean(images = images)
        assertEquals(1, bean.images.size)
        assertEquals(CoffeeBeanImageType.MAIN, bean.images[0].type)
    }
}
