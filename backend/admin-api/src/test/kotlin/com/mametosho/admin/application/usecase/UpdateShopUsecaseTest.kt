package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.UpdateShopRequest
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shared.ParticipationStatus
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.Prefecture
import com.mametosho.domain.model.shop.ShopifyShopId
import com.mametosho.domain.model.shop.ShopId as ShopIdModel
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.repository.ShopRepository
import com.mametosho.domain.service.ShopDropDomainService
import com.mametosho.admin.test.FakeImageStorageService
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
        shopUrl = "https://existing.example.com",
        prefecture = Prefecture.TOKYO,
        participationStatus = ParticipationStatus.PARTICIPATING,
        images = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/old-image.png"),
            ),
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000012"),
                type = ShopImageType.LOGO,
                imageUrl = ImageUrl("https://example.com/logo.png"),
            ),
        ),
    )

    private val droppedShop = existingShop.copy(participationStatus = ParticipationStatus.DROPPED)

    private val savedShops = mutableListOf<Shop>()

    private val fakeRepository = object : ShopRepository {
        var shopToReturn: Shop? = existingShop

        override fun save(shop: Shop) {
            savedShops.add(shop)
        }

        override fun findById(id: ShopId): Shop? {
            return when (id.value) {
                existingShopId -> shopToReturn
                else -> null
            }
        }

        override fun findAll(
            page: Int,
            size: Int,
            name: String?,
            participationStatus: ParticipationStatus?,
        ): Pair<List<Shop>, Long> = Pair(emptyList(), 0L)
        override fun deleteById(id: ShopId) = Unit
    }

    private val fakeCoffeeBeanRepository = object : CoffeeBeanRepository {
        override fun save(coffeeBean: CoffeeBean) = Unit
        override fun findById(id: CoffeeBeanId): CoffeeBean? = null
        override fun deleteById(id: CoffeeBeanId) = Unit
        override fun invalidateByShopId(shopId: ShopIdModel) = Unit
    }

    private val fakeShopDropDomainService = ShopDropDomainService(fakeCoffeeBeanRepository)

    private val usecase = UpdateShopUsecase(fakeRepository, fakeShopDropDomainService, FakeImageStorageService)

    private fun createRequest(
        shopifyShopId: String = "updated-shop-001",
        name: String = "更新店舗",
        introduction: String? = "更新紹介文",
        particular: String? = "更新こだわり",
        shopUrl: String = "https://updated.example.com",
        prefecture: String = "OSAKA",
        participationStatus: String = "PARTICIPATING",
    ): UpdateShopRequest = UpdateShopRequest(
        shopifyShopId = shopifyShopId,
        name = name,
        introduction = introduction,
        particular = particular,
        shopUrl = shopUrl,
        prefecture = prefecture,
        participationStatus = participationStatus,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest(), emptyList(), emptyList())

            assertNotNull(shop)
            assertEquals(existingShopId, shop.id.value)
            assertEquals("updated-shop-001", shop.shopifyShopId.value)
            assertEquals("更新店舗", shop.name)
            assertEquals("更新紹介文", shop.introduction)
            assertEquals("更新こだわり", shop.particular)
        }

        @Test
        fun `編集後もShopIdが変わらない`() {
            val shop = usecase.execute(existingShopId, createRequest(), emptyList(), emptyList())

            assertNotNull(shop)
            assertEquals(existingShopId, shop.id.value)
        }
    }

    @Nested
    inner class 存在しないID {
        @Test
        fun `存在しないIDの場合はnullが返る`() {
            val shop = usecase.execute("00000000-0000-4000-8000-999999999999", createRequest(), emptyList(), emptyList())

            assertNull(shop)
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `introductionがnullでもShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest(introduction = null), emptyList(), emptyList())

            assertNotNull(shop)
            assertNull(shop.introduction)
        }

        @Test
        fun `particularがnullでもShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest(particular = null), emptyList(), emptyList())

            assertNotNull(shop)
            assertNull(shop.particular)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像なしでもShopを編集できる`() {
            val shop = usecase.execute(existingShopId, createRequest(), emptyList(), emptyList())

            assertNotNull(shop)
            assertEquals(2, shop.images.size, "画像未送信時は既存画像が維持される")
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `編集したShopがリポジトリに保存される`() {
            savedShops.clear()
            usecase.execute(existingShopId, createRequest(), emptyList(), emptyList())

            assertEquals(1, savedShops.size)
            assertEquals("更新店舗", savedShops[0].name)
        }

        @Test
        fun `存在しないIDの場合はリポジトリに保存されない`() {
            savedShops.clear()
            usecase.execute("00000000-0000-4000-8000-999999999999", createRequest(), emptyList(), emptyList())

            assertEquals(0, savedShops.size)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingShopId, createRequest(name = ""), emptyList(), emptyList())
            }
        }

        @Test
        fun `不正なUUID形式のIDの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute("invalid-id", createRequest(), emptyList(), emptyList())
            }
        }

        @Test
        fun `DROPPED状態の店舗を更新しようとすると例外が発生する`() {
            fakeRepository.shopToReturn = droppedShop
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingShopId, createRequest(participationStatus = "DROPPED"), emptyList(), emptyList())
            }
        }

        @Test
        fun `BEFORE_PARTICIPATIONからDROPPEDへの直接遷移は例外が発生する`() {
            fakeRepository.shopToReturn = existingShop.copy(participationStatus = ParticipationStatus.BEFORE_PARTICIPATION)
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingShopId, createRequest(participationStatus = "DROPPED"), emptyList(), emptyList())
            }
        }
    }
}
