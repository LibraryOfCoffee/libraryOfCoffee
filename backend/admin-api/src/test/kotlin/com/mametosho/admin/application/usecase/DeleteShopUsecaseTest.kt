package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shared.PublishStatus
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.Prefecture
import com.mametosho.domain.model.shop.ShopifyShopId
import com.mametosho.domain.repository.ShopRepository
import com.mametosho.admin.test.FakeImageStorageService
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeleteShopUsecaseTest {

    private val existingShopId = "00000000-0000-4000-8000-000000000001"

    private val existingShop = Shop(
        id = ShopId(existingShopId),
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

    private val deletedIds = mutableListOf<ShopId>()

    private val fakeRepository = object : ShopRepository {
        override fun save(shop: Shop) = Unit

        override fun findById(id: ShopId): Shop? {
            return if (id.value == existingShopId) existingShop else null
        }

        override fun findAll(
            page: Int,
            size: Int,
            name: String?,
            publishStatus: PublishStatus?,
        ): Pair<List<Shop>, Long> = Pair(emptyList(), 0L)
        override fun deleteById(id: ShopId) {
            deletedIds.add(id)
        }
    }

    private val usecase = DeleteShopUsecase(fakeRepository, FakeImageStorageService)

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にShopを削除できる`() {
            val result = usecase.execute(existingShopId)

            assertTrue(result)
        }
    }

    @Nested
    inner class 存在しないID {
        @Test
        fun `存在しないIDの場合はfalseが返る`() {
            val result = usecase.execute("00000000-0000-4000-8000-999999999999")

            assertFalse(result)
        }
    }

    @Nested
    inner class リポジトリ削除 {
        @Test
        fun `削除したShopのIDでdeleteByIdが呼ばれる`() {
            deletedIds.clear()
            usecase.execute(existingShopId)

            assertEquals(1, deletedIds.size)
            assertEquals(existingShopId, deletedIds[0].value)
        }

        @Test
        fun `存在しないIDの場合はdeleteByIdが呼ばれない`() {
            deletedIds.clear()
            usecase.execute("00000000-0000-4000-8000-999999999999")

            assertEquals(0, deletedIds.size)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `不正なUUID形式のIDの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute("invalid-id")
            }
        }
    }
}
