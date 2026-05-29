package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoffeeBeanTest {

    private val defaultTastes = listOf(
        CoffeeBeanTaste(
            id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000101"),
            tasteId = TasteId("00000000-0000-4000-8000-000000000041"),
            evaluationValue = 3,
        ),
    )

    private val defaultImages = listOf(
        CoffeeBeanImage(
            id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000201"),
            type = CoffeeBeanImageType.MAIN,
            imageUrl = com.mametosho.domain.model.shared.ImageUrl("https://example.com/bean.jpg"),
        ),
    )

    private fun createCoffeeBean(
        tastes: List<CoffeeBeanTaste> = defaultTastes,
        images: List<CoffeeBeanImage> = defaultImages,
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

    @Nested
    inner class 生成テスト {

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

    @Nested
    inner class テイストの一意性 {

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
    }

    @Nested
    inner class update {

        @Test
        fun `idが保持されshopIdが更新される`() {
            val bean = createCoffeeBean()
            val newShopId = "00000000-0000-4000-8000-000000000099"
            val updated = bean.update(
                shopId = newShopId,
                shopifyBeanId = "updated-bean-001",
                name = "更新後の豆",
                description = "更新後の説明",
                origin = "ブラジル",
                farm = "更新後農園",
                roastLevel = "FRENCH",
                processingMethod = "NATURAL",
                isSpecialty = true,
                images = listOf("MAIN" to "https://example.com/updated.png"),
                tastes = listOf("00000000-0000-4000-8000-000000000041" to 3),
            )
            assertEquals(bean.id, updated.id)
            assertEquals(ShopId(newShopId), updated.shopId)
        }

        @Test
        fun `各フィールドが更新される`() {
            val bean = createCoffeeBean()
            val updated = bean.update(
                shopId = "00000000-0000-4000-8000-000000000003",
                shopifyBeanId = "updated-bean-001",
                name = "更新後の豆",
                description = "更新後の説明",
                origin = "ブラジル",
                farm = "更新後農園",
                roastLevel = "FRENCH",
                processingMethod = "NATURAL",
                isSpecialty = true,
                images = listOf("MAIN" to "https://example.com/updated.png"),
                tastes = listOf("00000000-0000-4000-8000-000000000004" to 5),
            )
            assertEquals("updated-bean-001", updated.shopifyBeanId.value)
            assertEquals("更新後の豆", updated.name)
            assertEquals("更新後の説明", updated.description)
            assertEquals("ブラジル", updated.origin)
            assertEquals("更新後農園", updated.farm)
            assertEquals(RoastLevel.FRENCH, updated.roastLevel)
            assertEquals(ProcessingMethod.NATURAL, updated.processingMethod)
            assertTrue(updated.isSpecialty)
            assertEquals(1, updated.images.size)
            assertEquals(CoffeeBeanImageType.MAIN, updated.images[0].type)
            assertEquals("https://example.com/updated.png", updated.images[0].imageUrl.value)
            assertEquals(1, updated.tastes.size)
            assertEquals("00000000-0000-4000-8000-000000000004", updated.tastes[0].tasteId.value)
            assertEquals(5, updated.tastes[0].evaluationValue)
        }

        @Test
        fun `子エンティティのIDが新規生成される`() {
            val bean = createCoffeeBean()
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            val updated = bean.update(
                shopId = "00000000-0000-4000-8000-000000000003",
                shopifyBeanId = "shopify-bean-1",
                name = "エチオピア イルガチェフェ",
                description = "フルーティーな香りが特徴",
                origin = "エチオピア",
                farm = null,
                roastLevel = "LIGHT",
                processingMethod = "WASHED",
                isSpecialty = false,
                images = listOf("MAIN" to "https://example.com/bean.jpg"),
                tastes = listOf("00000000-0000-4000-8000-000000000004" to 3),
            )
            assertTrue(uuidRegex.matches(updated.images[0].id.value))
            assertTrue(uuidRegex.matches(updated.tastes[0].id.value))
        }
    }

    @Nested
    inner class バリデーション {

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
    }
}
