package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.query.ShopQueryService
import com.mametosho.admin.application.query.result.PagedResult
import com.mametosho.admin.application.query.result.ShopListResult
import com.mametosho.admin.application.usecase.CreateShopUsecase
import com.mametosho.admin.application.usecase.DeleteShopUsecase
import com.mametosho.admin.application.usecase.GetShopUsecase
import com.mametosho.admin.application.usecase.ListShopsUsecase
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
import com.mametosho.admin.test.FakeImageStorageService
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
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

    private val sampleListResult = PagedResult(
        items = listOf(
            ShopListResult(
                id = "00000000-0000-4000-8000-000000000001",
                shopifyShopId = "test-shop-001",
                name = "テスト店舗",
                introduction = "テスト紹介文",
                particular = "テストこだわり",
            ),
        ),
        totalCount = 1L,
        page = 0,
        size = 20,
    )

    private val emptyListResult = PagedResult<ShopListResult>(
        items = emptyList(),
        totalCount = 0L,
        page = 0,
        size = 20,
    )

    private val fakeQueryService = object : ShopQueryService {
        override fun findList(page: Int, size: Int): PagedResult<ShopListResult> = sampleListResult
    }

    private val fakeShopRepository = object : ShopRepository {
        override fun save(shop: Shop) = Unit
        override fun findById(id: ShopId): Shop? = null
        override fun deleteById(id: ShopId) = Unit
    }

    private val fakeImageStorageService = FakeImageStorageService

    private fun createController(
        createShop: Shop = sampleShop,
        getResult: Shop? = sampleShop,
        listResult: PagedResult<ShopListResult> = sampleListResult,
        updateShop: Shop? = sampleShop,
        deleteShopResult: Boolean = true,
    ): ShopController {
        val fakeGetUsecase = object : GetShopUsecase(fakeShopRepository) {
            override fun execute(id: String): Shop? = getResult
        }
        val fakeListUsecase = object : ListShopsUsecase(fakeQueryService) {
            override fun execute(page: Int, size: Int): PagedResult<ShopListResult> = listResult
        }
        val fakeCreateUsecase = object : CreateShopUsecase(fakeShopRepository, fakeImageStorageService) {
            override fun execute(request: CreateShopRequest, imageFiles: List<MultipartFile>, imageTypes: List<String>): Shop = createShop
        }
        val fakeUpdateUsecase = object : UpdateShopUsecase(fakeShopRepository, fakeImageStorageService) {
            override fun execute(id: String, request: UpdateShopRequest, imageFiles: List<MultipartFile>, imageTypes: List<String>): Shop? = updateShop
        }
        val fakeDeleteUsecase = object : DeleteShopUsecase(fakeShopRepository, fakeImageStorageService) {
            override fun execute(id: String): Boolean = deleteShopResult
        }
        return ShopController(fakeGetUsecase, fakeListUsecase, fakeCreateUsecase, fakeUpdateUsecase, fakeDeleteUsecase)
    }

    @Nested
    inner class 店舗一覧取得 {
        @Test
        fun `正常に店舗一覧を取得すると200が返る`() {
            val controller = createController()
            val response = controller.listShops(0, 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val controller = createController(listResult = emptyListResult)
            val response = controller.listShops(0, 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }
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
            )

            val response = controller.createShop(request, emptyList(), emptyList())

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
            )

            val response = controller.updateShop("00000000-0000-4000-8000-000000000001", request, emptyList(), emptyList())

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
            )

            val response = controller.updateShop("00000000-0000-4000-8000-999999999999", request, emptyList(), emptyList())

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
