package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.Image
import com.mametosho.domain.model.shared.ParticipationStatus
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ShopTest {

    private val defaultLogoImage = ShopImage(
        id = ShopImageId("00000000-0000-4000-8000-000000000008"),
        type = ShopImageType.LOGO,
        image = Image("https://example.com/logo.png"),
    )

    private fun createShop(
        images: List<ShopImage> = listOf(defaultLogoImage),
        shopUrl: String = "https://example.com",
        participationStatus: ParticipationStatus = ParticipationStatus.PARTICIPATING,
    ): Shop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000003"),
        shopifyShopId = ShopifyShopId("shopify-shop-1"),
        name = "珈琲工房まめ図書",
        introduction = "こだわりの珈琲豆をお届けします",
        particular = "産地直送の豆を使用",
        shopUrl = shopUrl,
        prefecture = Prefecture.TOKYO,
        participationStatus = participationStatus,
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
                defaultLogoImage,
                ShopImage(
                    id = ShopImageId("00000000-0000-4000-8000-000000000009"),
                    type = ShopImageType.MAIN,
                    image = Image("https://example.com/shop.jpg"),
                ),
            )
            val shop = createShop(images = images)
            assertEquals(2, shop.images.size)
        }
    }

    @Nested
    inner class update {

        @Test
        fun `正常に店舗情報を更新できる`() {
            val shop = createShop(participationStatus = ParticipationStatus.BEFORE_PARTICIPATION)
            val updated = shop.update(
                shopifyShopId = "updated-shop-id",
                name = "更新店舗",
                introduction = "更新紹介文",
                particular = "更新こだわり",
                shopUrl = "https://updated.example.com",
                prefecture = Prefecture.OSAKA,
                participationStatus = "PARTICIPATING",
                images = listOf("LOGO" to "https://example.com/logo.png", "MAIN" to "https://example.com/new.png"),
            )

            assertEquals(shop.id, updated.id)
            assertEquals("updated-shop-id", updated.shopifyShopId.value)
            assertEquals("更新店舗", updated.name)
            assertEquals("更新紹介文", updated.introduction)
            assertEquals("更新こだわり", updated.particular)
            assertEquals("https://updated.example.com", updated.shopUrl)
            assertEquals(2, updated.images.size)
        }

        @Test
        fun `更新後もShopIdが変わらない`() {
            val shop = createShop()
            val updated = shop.update(
                shopifyShopId = "new-shop-id",
                name = "新店舗",
                introduction = null,
                particular = null,
                shopUrl = "https://example.com",
                prefecture = Prefecture.TOKYO,
                participationStatus = "PARTICIPATING",
                images = listOf("LOGO" to "https://example.com/logo.png"),
            )

            assertEquals(shop.id, updated.id)
        }

        @Test
        fun `更新時にShopImageIdがUUID形式で自動再生成される`() {
            val shop = createShop()
            val updated = shop.update(
                shopifyShopId = "shopify-shop-1",
                name = "珈琲工房まめ図書",
                introduction = null,
                particular = null,
                shopUrl = "https://example.com",
                prefecture = Prefecture.TOKYO,
                participationStatus = "PARTICIPATING",
                images = listOf("LOGO" to "https://example.com/logo.png", "MAIN" to "https://example.com/image.png"),
            )

            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(updated.images[0].id.value))
        }

        @Test
        fun `BEFORE_PARTICIPATIONからPARTICIPATINGへの遷移ができる`() {
            val shop = createShop(participationStatus = ParticipationStatus.BEFORE_PARTICIPATION)
            val updated = shop.update(
                shopifyShopId = "shopify-shop-1",
                name = "珈琲工房まめ図書",
                introduction = null,
                particular = null,
                shopUrl = "https://example.com",
                prefecture = Prefecture.TOKYO,
                participationStatus = "PARTICIPATING",
                images = listOf("LOGO" to "https://example.com/logo.png"),
            )
            assertEquals(ParticipationStatus.PARTICIPATING, updated.participationStatus)
        }

        @Test
        fun `PARTICIPATINGからDROPPEDへの遷移ができる`() {
            val shop = createShop(participationStatus = ParticipationStatus.PARTICIPATING)
            val updated = shop.update(
                shopifyShopId = "shopify-shop-1",
                name = "珈琲工房まめ図書",
                introduction = null,
                particular = null,
                shopUrl = "https://example.com",
                prefecture = Prefecture.TOKYO,
                participationStatus = "DROPPED",
                images = listOf("LOGO" to "https://example.com/logo.png"),
            )
            assertEquals(ParticipationStatus.DROPPED, updated.participationStatus)
        }

        @Test
        fun `DROPPED状態からの変更は例外が発生する`() {
            val shop = createShop(participationStatus = ParticipationStatus.DROPPED)
            assertThrows<IllegalArgumentException> {
                shop.update(
                    shopifyShopId = "shopify-shop-1",
                    name = "珈琲工房まめ図書",
                    introduction = null,
                    particular = null,
                    shopUrl = "https://example.com",
                    prefecture = Prefecture.TOKYO,
                    participationStatus = "PARTICIPATING",
                    images = listOf("LOGO" to "https://example.com/logo.png"),
                )
            }
        }

        @Test
        fun `BEFORE_PARTICIPATIONからDROPPEDへの直接遷移は例外が発生する`() {
            val shop = createShop(participationStatus = ParticipationStatus.BEFORE_PARTICIPATION)
            assertThrows<IllegalArgumentException> {
                shop.update(
                    shopifyShopId = "shopify-shop-1",
                    name = "珈琲工房まめ図書",
                    introduction = null,
                    particular = null,
                    shopUrl = "https://example.com",
                    prefecture = Prefecture.TOKYO,
                    participationStatus = "DROPPED",
                    images = listOf("LOGO" to "https://example.com/logo.png"),
                )
            }
        }

        @Test
        fun `PARTICIPATINGからBEFORE_PARTICIPATIONへの逆遷移は例外が発生する`() {
            val shop = createShop(participationStatus = ParticipationStatus.PARTICIPATING)
            assertThrows<IllegalArgumentException> {
                shop.update(
                    shopifyShopId = "shopify-shop-1",
                    name = "珈琲工房まめ図書",
                    introduction = null,
                    particular = null,
                    shopUrl = "https://example.com",
                    prefecture = Prefecture.TOKYO,
                    participationStatus = "BEFORE_PARTICIPATION",
                    images = listOf("LOGO" to "https://example.com/logo.png"),
                )
            }
        }

        @Test
        fun `DROPPED状態でステータス変更なしでも例外が発生する`() {
            val shop = createShop(participationStatus = ParticipationStatus.DROPPED)
            assertThrows<IllegalArgumentException> {
                shop.update(
                    shopifyShopId = "shopify-shop-1",
                    name = "珈琲工房まめ図書",
                    introduction = null,
                    particular = null,
                    shopUrl = "https://example.com",
                    prefecture = Prefecture.TOKYO,
                    participationStatus = "DROPPED",
                    images = listOf("LOGO" to "https://example.com/logo.png"),
                )
            }
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

        @Test
        fun `shopUrlが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop(shopUrl = "")
            }
        }

        @Test
        fun `shopUrlが2049文字以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop(shopUrl = "https://example.com/" + "a".repeat(2029))
            }
        }

        @Test
        fun `LOGO画像が0枚の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop(images = emptyList())
            }
        }

        @Test
        fun `LOGO画像が2枚以上の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createShop(
                    images = listOf(
                        ShopImage(
                            id = ShopImageId("00000000-0000-4000-8000-000000000009"),
                            type = ShopImageType.LOGO,
                            image = Image("https://example.com/logo1.png"),
                        ),
                        ShopImage(
                            id = ShopImageId("00000000-0000-4000-8000-000000000010"),
                            type = ShopImageType.LOGO,
                            image = Image("https://example.com/logo2.png"),
                        ),
                    ),
                )
            }
        }
    }
}
