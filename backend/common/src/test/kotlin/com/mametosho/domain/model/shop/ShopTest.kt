package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.ImageUrl
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShopTest {

    private fun createShop(
        images: List<ShopImage> = emptyList(),
    ): Shop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000003"),
        shopifyShopId = ShopifyShopId("shopify-shop-1"),
        name = "珈琲工房まめ図書",
        introduction = "こだわりの珈琲豆をお届けします",
        particular = "産地直送の豆を使用",
        images = images,
    )

    @Nested
    inner class 生成テスト {

        @Test
        fun `正常にShopを生成できる`() {
            val shop = createShop()
            assertEquals("珈琲工房まめ図書", shop.name)
            assertEquals("shopify-shop-1", shop.shopifyShopId.value)
        }

        @Test
        fun `introductionがnullでも生成できる`() {
            val shop = createShop().copy(introduction = null)
            assertNull(shop.introduction)
        }

        @Test
        fun `particularがnullでも生成できる`() {
            val shop = createShop().copy(particular = null)
            assertNull(shop.particular)
        }

        @Test
        fun `画像を持つShopを生成できる`() {
            val images = listOf(
                ShopImage(
                    id = ShopImageId("00000000-0000-4000-8000-000000000009"),
                    type = ShopImageType.MAIN,
                    imageUrl = ImageUrl("https://example.com/shop.jpg"),
                ),
            )
            val shop = createShop(images = images)
            assertEquals(1, shop.images.size)
            assertEquals(ShopImageType.MAIN, shop.images[0].type)
        }
    }

    @Nested
    inner class バリデーション {

        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop().copy(name = "")
            }
        }

        @Test
        fun `nameがスペースのみの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop().copy(name = "   ")
            }
        }

        @Test
        fun `nameが256文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop().copy(name = "a".repeat(256))
            }
        }

        @Test
        fun `introductionが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop().copy(introduction = "")
            }
        }

        @Test
        fun `introductionが10001文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop().copy(introduction = "a".repeat(10001))
            }
        }

        @Test
        fun `particularが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop().copy(particular = "")
            }
        }

        @Test
        fun `particularが10001文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop().copy(particular = "a".repeat(10001))
            }
        }
    }
}
