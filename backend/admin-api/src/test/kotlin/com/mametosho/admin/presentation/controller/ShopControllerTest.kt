package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.CreateShopUsecase
import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopControllerTest {

    private val sampleShop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000001"),
        shopifyShopId = ShopifyShopId("test-shop-001"),
        name = "テスト店舗",
        introduction = "テスト紹介文",
        particular = "テストこだわり",
        images = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/image.png"),
            ),
        ),
    )

    private fun createController(shop: Shop = sampleShop): ShopController {
        val fakeUsecase = object : CreateShopUsecase(
            object : com.mametosho.domain.repository.ShopRepository {
                override fun save(shop: Shop) = Unit
            },
        ) {
            override fun execute(request: CreateShopRequest): Shop = shop
        }
        return ShopController(fakeUsecase)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `正常に店舗を登録すると201が返る`() {
            val controller = createController()
            val request = CreateShopRequest(
                shopifyShopId = "test-shop-001",
                name = "テスト店舗",
                introduction = "テスト紹介文",
                particular = "テストこだわり",
                images = listOf(
                    CreateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/image.png"),
                ),
            )

            val response = controller.createShop(request)

            assertEquals(HttpStatus.CREATED, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        }
    }
}
