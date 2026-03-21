package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.repository.ShopRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CreateShopUsecaseTest {

    private val savedShops = mutableListOf<Shop>()

    private val fakeRepository = object : ShopRepository {
        override fun save(shop: Shop) {
            savedShops.add(shop)
        }

        override fun findById(id: ShopId): Shop? = null
        override fun deleteById(id: ShopId) = Unit
    }

    private val usecase = CreateShopUsecase(fakeRepository)

    private fun createRequest(
        shopifyShopId: String = "test-shop-001",
        name: String = "テスト店舗",
        introduction: String? = "テスト紹介文",
        particular: String? = "テストこだわり",
        images: List<CreateShopRequest.ImageRequest> = listOf(
            CreateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/image.png"),
        ),
    ): CreateShopRequest = CreateShopRequest(
        shopifyShopId = shopifyShopId,
        name = name,
        introduction = introduction,
        particular = particular,
        images = images,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にShopを作成できる`() {
            val request = createRequest()
            val shop = usecase.execute(request)

            assertEquals("test-shop-001", shop.shopifyShopId.value)
            assertEquals("テスト店舗", shop.name)
            assertEquals("テスト紹介文", shop.introduction)
            assertEquals("テストこだわり", shop.particular)
            assertEquals(1, shop.images.size)
            assertEquals(ShopImageType.MAIN, shop.images[0].type)
            assertEquals("https://example.com/image.png", shop.images[0].imageUrl.value)
        }
    }

    @Nested
    inner class UUID自動生成 {
        @Test
        fun `ShopIdがUUID形式で自動生成される`() {
            val shop = usecase.execute(createRequest())
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(shop.id.value))
        }

        @Test
        fun `ShopImageIdがUUID形式で自動生成される`() {
            val shop = usecase.execute(createRequest())
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(shop.images[0].id.value))
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `introductionがnullでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(introduction = null))
            assertNull(shop.introduction)
        }

        @Test
        fun `particularがnullでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(particular = null))
            assertNull(shop.particular)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像なしでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(images = emptyList()))
            assertEquals(0, shop.images.size)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `作成したShopがリポジトリに保存される`() {
            savedShops.clear()
            usecase.execute(createRequest())
            assertEquals(1, savedShops.size)
            assertEquals("テスト店舗", savedShops[0].name)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(name = ""))
            }
        }

        @Test
        fun `不正な画像種別の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(images = listOf(
                    CreateShopRequest.ImageRequest(type = "INVALID", imageUrl = "https://example.com/image.png"),
                )))
            }
        }
    }
}
