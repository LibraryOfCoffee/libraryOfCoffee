package com.mametosho.cs.application.usecase

import com.mametosho.domain.model.shared.Image
import com.mametosho.domain.model.shared.ParticipationStatus
import com.mametosho.domain.model.shop.Prefecture
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import com.mametosho.domain.repository.ShopRepository
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class GetShopUsecaseTest {

    private fun sampleShop(participationStatus: ParticipationStatus) = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000031"),
        shopifyShopId = ShopifyShopId("test-shop-001"),
        name = "珈琲工房 まめとしょ",
        introduction = "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
        particular = "厳選された豆のみを使用しています。",
        shopUrl = "https://mametosho.example.com",
        prefecture = Prefecture.TOKYO,
        participationStatus = participationStatus,
        images = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000091"),
                type = ShopImageType.LOGO,
                image = Image("https://placehold.jp/100x100.png"),
            ),
        ),
    )

    private fun createUsecase(shop: Shop?): GetShopUsecase {
        val fakeRepository = object : ShopRepository {
            override fun save(shop: Shop) = Unit
            override fun findById(id: ShopId): Shop? = shop
            override fun deleteById(id: ShopId) = Unit
            override fun findAll(
                page: Int,
                size: Int,
                name: String?,
                participationStatus: ParticipationStatus?,
            ): Pair<List<Shop>, Long> = Pair(emptyList(), 0L)
        }
        return GetShopUsecase(fakeRepository)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `参画中の店舗を取得できる`() {
            val usecase = createUsecase(sampleShop(ParticipationStatus.PARTICIPATING))

            val result = usecase.execute("00000000-0000-4000-8000-000000000031")

            assertEquals("00000000-0000-4000-8000-000000000031", result?.id?.value)
        }
    }

    @Nested
    inner class 異常系 {
        @Test
        fun `参画前の店舗はnullを返す`() {
            val usecase = createUsecase(sampleShop(ParticipationStatus.BEFORE_PARTICIPATION))

            assertNull(usecase.execute("00000000-0000-4000-8000-000000000031"))
        }

        @Test
        fun `参画落ちの店舗はnullを返す`() {
            val usecase = createUsecase(sampleShop(ParticipationStatus.DROPPED))

            assertNull(usecase.execute("00000000-0000-4000-8000-000000000031"))
        }

        @Test
        fun `存在しない店舗はnullを返す`() {
            val usecase = createUsecase(null)

            assertNull(usecase.execute("00000000-0000-4000-8000-000000000999"))
        }

        @Test
        fun `UUID形式でないIDはIllegalArgumentExceptionを投げる`() {
            val usecase = createUsecase(null)

            assertFailsWith<IllegalArgumentException> {
                usecase.execute("invalid-id")
            }
        }
    }
}
