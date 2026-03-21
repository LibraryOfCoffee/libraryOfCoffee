package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.CreateShopUsecase
import com.mametosho.admin.application.usecase.DeleteShopUsecase
import com.mametosho.admin.application.usecase.UpdateShopUsecase
import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.admin.presentation.dto.request.UpdateShopRequest
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import com.mametosho.domain.repository.ShopRepository
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

    private val fakeShopRepository = object : ShopRepository {
        override fun save(shop: Shop) = Unit
        override fun findById(id: ShopId): Shop? = null
        override fun deleteById(id: ShopId) = Unit
    }

    private fun createController(
        createShop: Shop = sampleShop,
        updateShop: Shop? = sampleShop,
        deleteShopResult: Boolean = true,
    ): ShopController {
        val fakeCreateUsecase = object : CreateShopUsecase(fakeShopRepository) {
            override fun execute(request: CreateShopRequest): Shop = createShop
        }
        val fakeUpdateUsecase = object : UpdateShopUsecase(fakeShopRepository) {
            override fun execute(id: String, request: UpdateShopRequest): Shop? = updateShop
        }
        val fakeDeleteUsecase = object : DeleteShopUsecase(fakeShopRepository) {
            override fun execute(id: String): Boolean = deleteShopResult
        }
        return ShopController(fakeCreateUsecase, fakeUpdateUsecase, fakeDeleteUsecase)
    }

    @Nested
    inner class 店舗登録 {
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

    @Nested
    inner class 店舗編集 {
        @Test
        fun `正常に店舗を編集すると200が返る`() {
            val controller = createController()
            val request = UpdateShopRequest(
                shopifyShopId = "test-shop-001",
                name = "テスト店舗",
                introduction = "テスト紹介文",
                particular = "テストこだわり",
                images = listOf(
                    UpdateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/image.png"),
                ),
            )

            val response = controller.updateShop("00000000-0000-4000-8000-000000000001", request)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        }

        @Test
        fun `存在しない店舗を編集すると404が返る`() {
            val controller = createController(updateShop = null)
            val request = UpdateShopRequest(
                shopifyShopId = "test-shop-001",
                name = "テスト店舗",
                introduction = null,
                particular = null,
                images = emptyList(),
            )

            val response = controller.updateShop("00000000-0000-4000-8000-999999999999", request)

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertNull(response.body)
        }
    }

    @Nested
    inner class 店舗削除 {
        @Test
        fun `正常に店舗を削除すると204が返る`() {
            val controller = createController(deleteShopResult = true)

            val response = controller.deleteShop("00000000-0000-4000-8000-000000000001")

            assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
            assertNull(response.body)
        }

        @Test
        fun `存在しない店舗を削除すると404が返る`() {
            val controller = createController(deleteShopResult = false)

            val response = controller.deleteShop("00000000-0000-4000-8000-999999999999")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertNull(response.body)
        }
    }
}
