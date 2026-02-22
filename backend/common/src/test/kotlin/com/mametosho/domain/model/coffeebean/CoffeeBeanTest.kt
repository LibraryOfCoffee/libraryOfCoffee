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
        id = CoffeeBeanId("01J000000000000000000BEAN1"),
        shopId = ShopId("01J000000000000000000SHOP1"),
        shopifyBeanId = ShopifyBeanId("shopify-bean-1"),
        name = "エチオピア イルガチェフェ",
        description = "フルーティーな香りが特徴",
        origin = "エチオピア",
        farm = "イルガチェフェ農園",
        roastLevel = RoastLevel.LIGHT,
        processingMethod = ProcessingMethod.WASHED,
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
                id = CoffeeBeanTasteId("01J0000000000000000TASTE01"),
                tasteId = TasteId("01J00000000000000000TASTE1"),
                evaluationValue = 3,
            ),
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("01J0000000000000000TASTE02"),
                tasteId = TasteId("01J00000000000000000TASTE2"),
                evaluationValue = 5,
            ),
        )
        val bean = createCoffeeBean(tastes = tastes)
        assertEquals(2, bean.tastes.size)
    }

    @Test
    fun `同じTasteIdのテイスト評価が重複する場合は例外が発生する`() {
        val duplicateTasteId = TasteId("01J00000000000000000TASTE1")
        val tastes = listOf(
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("01J0000000000000000TASTE01"),
                tasteId = duplicateTasteId,
                evaluationValue = 3,
            ),
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("01J0000000000000000TASTE02"),
                tasteId = duplicateTasteId,
                evaluationValue = 5,
            ),
        )
        assertThrows<IllegalArgumentException> {
            createCoffeeBean(tastes = tastes)
        }
    }

    @Test
    fun `画像を持つCoffeeBeanを生成できる`() {
        val images = listOf(
            CoffeeBeanImage(
                id = CoffeeBeanImageId("01J0000000000000000IMAGE01"),
                type = CoffeeBeanImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/bean.jpg"),
            ),
        )
        val bean = createCoffeeBean(images = images)
        assertEquals(1, bean.images.size)
        assertEquals(CoffeeBeanImageType.MAIN, bean.images[0].type)
    }
}
