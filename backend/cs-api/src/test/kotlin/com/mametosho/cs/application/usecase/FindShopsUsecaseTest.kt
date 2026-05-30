package com.mametosho.cs.application.usecase

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Prefecture
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import com.mametosho.domain.model.PagedResult
import com.mametosho.domain.repository.ShopRepository
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class FindShopsUsecaseTest {

    private var capturedPage: Int? = null
    private var capturedSize: Int? = null

    private val sampleShop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000031"),
        shopifyShopId = ShopifyShopId("test-shop-001"),
        name = "珈琲工房 まめとしょ",
        introduction = "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
        particular = null,
        shopUrl = "https://mametosho.example.com",
        prefecture = Prefecture.TOKYO,
        images = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000091"),
                type = ShopImageType.LOGO,
                imageUrl = ImageUrl("https://placehold.jp/100x100.png"),
            ),
        ),
    )

    private fun createUsecase(
        shops: List<Shop> = listOf(sampleShop),
        totalCount: Long = 1L,
    ): FindShopsUsecase {
        val fakeRepository = object : ShopRepository {
            override fun save(shop: Shop) = Unit
            override fun findById(id: ShopId) = null
            override fun deleteById(id: ShopId) = Unit
            override fun findAll(page: Int, size: Int, name: String?): PagedResult<Shop> {
                capturedPage = page
                capturedSize = size
                return PagedResult(shops, totalCount, page, size)
            }
        }
        return FindShopsUsecase(fakeRepository)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `pageとsizeを指定して一覧を取得できる`() {
            val usecase = createUsecase()

            val result = usecase.execute(page = 0, size = 20)

            assertEquals(1, result.items.size)
            assertEquals(0, capturedPage)
            assertEquals(20, capturedSize)
        }

        @Test
        fun `ページネーションパラメータがRepositoryに正しく渡される`() {
            val usecase = createUsecase()

            usecase.execute(page = 2, size = 10)

            assertEquals(2, capturedPage)
            assertEquals(10, capturedSize)
        }
    }
}
