package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shared.ParticipationStatus
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
        participationStatus = ParticipationStatus.PARTICIPATING,
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
        fun `正常にShopからShopSummaryResponseに変換できる`() {
            val response = ShopSummaryResponse.from(createShop())
            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
            assertEquals("PARTICIPATING", response.participationStatus)
        }

        @Test
        fun `正常にShopからShopDetailResponseに変換できる`() {
            val response = ShopDetailResponse.from(createShop())
            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
            assertEquals("PARTICIPATING", response.participationStatus)
            assertEquals(2, response.images.size)
        }
    }
}
