package com.mametosho.admin.application.usecase

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
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UpdateShopUsecaseTest {

    private val existingShopId = "00000000-0000-4000-8000-000000000001"

    private val existingShop = Shop(
        id = ShopId(existingShopId),
        shopifyShopId = ShopifyShopId("existing-shop-001"),
        name = "既存店舗",
        introduction = "既存紹介文",
        particular = "既存こだわり",
        images = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/old-image.png"),
            ),
        ),
    )

    private val savedShops = mutableListOf<Shop>()

    private val fakeRepository = object : ShopRepository {
        override fun save(shop: Shop) {
            savedShops.add(shop)
        }

        override fun findById(id: ShopId): Shop? {
            return if (id.value == existingShopId) existingShop else null
        }

        override fun deleteById(id: ShopId) = Unit
    }

    private val usecase = UpdateShopUsecase(fakeRepository)

    private fun createRequest(
        shopifyShopId: String = "updated-shop-001",
        name: String = "更新店舗",
        introduction: String? = "更新紹介文",
        particular: String? = "更新こだわり",
        images: List<UpdateShopRequest.ImageRequest> = listOf(
            UpdateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/new-image.png"),
        ),
    ): UpdateShopRequest = UpdateShopRequest(
        shopifyShopId = shopifyShopId,
        name = name,
        introduction = introduction,
        particular = particular,
        images = images,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest())

            assertNotNull(shop)
            assertEquals(existingShopId, shop.id.value)
            assertEquals("updated-shop-001", shop.shopifyShopId.value)
            assertEquals("更新店舗", shop.name)
            assertEquals("更新紹介文", shop.introduction)
            assertEquals("更新こだわり", shop.particular)
            assertEquals(1, shop.images.size)
            assertEquals(ShopImageType.MAIN, shop.images[0].type)
            assertEquals("https://example.com/new-image.png", shop.images[0].imageUrl.value)
        }

        @Test
        fun `編集後もShopIdが変わらない`() {
            val shop = usecase.execute(existingShopId, createRequest())

            assertNotNull(shop)
            assertEquals(existingShopId, shop.id.value)
        }
    }

    @Nested
    inner class 存在しないID {
        @Test
        fun `存在しないIDの場合はnullが返る`() {
            val shop = usecase.execute("00000000-0000-4000-8000-999999999999", createRequest())

            assertNull(shop)
        }
    }

    @Nested
    inner class UUID自動生成 {
        @Test
        fun `ShopImageIdがUUID形式で自動再生成される`() {
            val shop = usecase.execute(existingShopId, createRequest())

            assertNotNull(shop)
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(shop.images[0].id.value))
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `introductionがnullでもShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest(introduction = null))

            assertNotNull(shop)
            assertNull(shop.introduction)
        }

        @Test
        fun `particularがnullでもShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest(particular = null))

            assertNotNull(shop)
            assertNull(shop.particular)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像なしでもShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest(images = emptyList()))

            assertNotNull(shop)
            assertEquals(0, shop.images.size)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `編集したShopがリポジトリに保存される`() {
            savedShops.clear()
            usecase.execute(existingShopId, createRequest())

            assertEquals(1, savedShops.size)
            assertEquals("更新店舗", savedShops[0].name)
        }

        @Test
        fun `存在しないIDの場合はリポジトリに保存されない`() {
            savedShops.clear()
            usecase.execute("00000000-0000-4000-8000-999999999999", createRequest())

            assertEquals(0, savedShops.size)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingShopId, createRequest(name = ""))
            }
        }

        @Test
        fun `不正な画像種別の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingShopId, createRequest(images = listOf(
                    UpdateShopRequest.ImageRequest(type = "INVALID", imageUrl = "https://example.com/image.png"),
                )))
            }
        }

        @Test
        fun `不正なUUID形式のIDの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute("invalid-id", createRequest())
            }
        }
    }
}
