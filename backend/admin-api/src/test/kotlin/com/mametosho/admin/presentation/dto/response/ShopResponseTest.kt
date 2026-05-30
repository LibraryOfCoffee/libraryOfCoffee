package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shared.PublishStatus
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.Prefecture
import com.mametosho.domain.model.shop.ShopifyShopId
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopResponseTest {

    private fun createShop(): Shop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000001"),
        shopifyShopId = ShopifyShopId("test-shop-001"),
        name = "テスト店舗",
        introduction = "テスト紹介文",
        particular = "テストこだわり",
        shopUrl = "https://example.com",
        prefecture = Prefecture.TOKYO,
        publishStatus = PublishStatus.PUBLISHED,
        images = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/image.png"),
            ),
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000012"),
                type = ShopImageType.LOGO,
                imageUrl = ImageUrl("https://example.com/logo.png"),
            ),
        ),
    )

    @Nested
    inner class 正常系変換 {
        @Test
        fun `正常にShopからShopResponseに変換できる`() {
            val response = ShopResponse.from(createShop())
            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
        }
    }
}
