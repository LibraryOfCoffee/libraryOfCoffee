package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShopResponseTest {

    private fun createShop(
        introduction: String? = "テスト紹介文",
        particular: String? = "テストこだわり",
        images: List<ShopImage> = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/image.png"),
            ),
        ),
    ): Shop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000001"),
        shopifyShopId = ShopifyShopId("test-shop-001"),
        name = "テスト店舗",
        introduction = introduction,
        particular = particular,
        images = images,
    )

    @Test
    fun `正常にShopからShopResponseに変換できる`() {
        val response = ShopResponse.from(createShop())

        assertEquals("00000000-0000-4000-8000-000000000001", response.id)
        assertEquals("test-shop-001", response.shopifyShopId)
        assertEquals("テスト店舗", response.name)
        assertEquals("テスト紹介文", response.introduction)
        assertEquals("テストこだわり", response.particular)
        assertEquals(1, response.images.size)
        assertEquals("00000000-0000-4000-8000-000000000011", response.images[0].id)
        assertEquals("MAIN", response.images[0].type)
        assertEquals("https://example.com/image.png", response.images[0].imageUrl)
    }

    @Test
    fun `introductionがnullの場合もレスポンスに変換できる`() {
        val response = ShopResponse.from(createShop(introduction = null))
        assertNull(response.introduction)
    }

    @Test
    fun `particularがnullの場合もレスポンスに変換できる`() {
        val response = ShopResponse.from(createShop(particular = null))
        assertNull(response.particular)
    }

    @Test
    fun `画像が空の場合もレスポンスに変換できる`() {
        val response = ShopResponse.from(createShop(images = emptyList()))
        assertEquals(0, response.images.size)
    }
}
