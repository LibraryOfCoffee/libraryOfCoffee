package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.ImageUpload
import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shared.ParticipationStatus
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.ShopRepository
import com.mametosho.admin.test.FakeImageStorageService
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
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
        override fun findAll(
            page: Int,
            size: Int,
            name: String?,
            participationStatus: ParticipationStatus?,
        ): Pair<List<Shop>, Long> = Pair(emptyList(), 0L)
        override fun deleteById(id: ShopId) = Unit
    }

    private val usecase = CreateShopUsecase(fakeRepository, FakeImageStorageService)

    private val logoFile = MockMultipartFile("images", "logo.png", "image/png", byteArrayOf(1))

    private fun createRequest(
        shopifyShopId: String = "test-shop-001",
        name: String = "テスト店舗",
        introduction: String? = "テスト紹介文",
        particular: String? = "テストこだわり",
        shopUrl: String = "https://example.com",
        prefecture: String = "TOKYO",
        participationStatus: String = "BEFORE_PARTICIPATION",
    ): CreateShopRequest = CreateShopRequest(
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
        fun `正常にShopを作成できる`() {
            val request = createRequest()
            val shop = usecase.execute(request, listOf(ImageUpload("LOGO", logoFile)))

            assertEquals("test-shop-001", shop.shopifyShopId.value)
            assertEquals("テスト店舗", shop.name)
            assertEquals("テスト紹介文", shop.introduction)
            assertEquals("テストこだわり", shop.particular)
        }
    }

    @Nested
    inner class UUID自動生成 {
        @Test
        fun `ShopIdがUUID形式で自動生成される`() {
            val shop = usecase.execute(createRequest(), listOf(ImageUpload("LOGO", logoFile)))
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(shop.id.value))
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `introductionがnullでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(introduction = null), listOf(ImageUpload("LOGO", logoFile)))
            assertNull(shop.introduction)
        }

        @Test
        fun `particularがnullでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(particular = null), listOf(ImageUpload("LOGO", logoFile)))
            assertNull(shop.particular)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `作成したShopがリポジトリに保存される`() {
            savedShops.clear()
            usecase.execute(createRequest(), listOf(ImageUpload("LOGO", logoFile)))
            assertEquals(1, savedShops.size)
            assertEquals("テスト店舗", savedShops[0].name)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(name = ""), listOf(ImageUpload("LOGO", logoFile)))
            }
        }

        @Test
        fun `LOGO画像なしの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(), emptyList())
            }
        }

        @Test
        fun `初期ステータスにDROPPEDを指定すると例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(participationStatus = "DROPPED"), listOf(ImageUpload("LOGO", logoFile)))
            }
        }
    }
}
