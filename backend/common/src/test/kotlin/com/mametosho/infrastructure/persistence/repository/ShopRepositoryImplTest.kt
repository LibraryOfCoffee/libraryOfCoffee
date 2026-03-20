package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest(classes = [com.mametosho.infrastructure.TestApplication::class])
@Testcontainers
@ActiveProfiles("test")
class ShopRepositoryImplTest {

    companion object {
        @Container
        @JvmStatic
        val mysql = MySQLContainer("mysql:8.4").apply {
            withDatabaseName("test")
            withUsername("test")
            withPassword("test")
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { mysql.jdbcUrl }
            registry.add("spring.datasource.username") { mysql.username }
            registry.add("spring.datasource.password") { mysql.password }
            registry.add("spring.datasource.driver-class-name") { mysql.driverClassName }
        }
    }

    @Autowired
    private lateinit var shopRepositoryImpl: ShopRepositoryImpl

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM shop_images")
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM coffee_bean_images")
        jdbcTemplate.execute("DELETE FROM coffee_list_childs")
        jdbcTemplate.execute("DELETE FROM coffee_beans")
        jdbcTemplate.execute("DELETE FROM shops")
    }

    private fun createShop(
        id: String = "00000000-0000-4000-8000-000000000001",
        shopifyShopId: String = "test-shop-001",
        introduction: String? = "テスト紹介文",
        particular: String? = "テストこだわり",
        images: List<ShopImage> = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/shop.png"),
            ),
        ),
    ): Shop = Shop(
        id = ShopId(id),
        shopifyShopId = ShopifyShopId(shopifyShopId),
        name = "テスト店舗",
        introduction = introduction,
        particular = particular,
        images = images,
    )

    @Nested
    inner class 正常系INSERT {
        @Test
        fun `正常にShopを保存できる`() {
            val shop = createShop()

            shopRepositoryImpl.save(shop)

            val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
            assertEquals(1, shops.size)
            assertEquals("00000000-0000-4000-8000-000000000001", shops[0]["id"])
            assertEquals("test-shop-001", shops[0]["shopify_shop_id"])
            assertEquals("テスト店舗", shops[0]["name"])
            assertEquals("テスト紹介文", shops[0]["introduction"])
            assertEquals("テストこだわり", shops[0]["particular"])
        }
    }

    @Nested
    inner class 子テーブルINSERT {
        @Test
        fun `画像が正しく保存される`() {
            val shop = createShop()

            shopRepositoryImpl.save(shop)

            val images = jdbcTemplate.queryForList("SELECT * FROM shop_images")
            assertEquals(1, images.size)
            assertEquals("00000000-0000-4000-8000-000000000011", images[0]["id"])
            assertEquals("00000000-0000-4000-8000-000000000001", images[0]["shop_id"])
            assertEquals("main", images[0]["type"])
            assertEquals("https://example.com/shop.png", images[0]["image_url"])
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `introductionがnullでも保存できる`() {
            val shop = createShop(introduction = null)

            shopRepositoryImpl.save(shop)

            val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
            assertEquals(1, shops.size)
            assertEquals(null, shops[0]["introduction"])
        }

        @Test
        fun `particularがnullでも保存できる`() {
            val shop = createShop(particular = null)

            shopRepositoryImpl.save(shop)

            val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
            assertEquals(1, shops.size)
            assertEquals(null, shops[0]["particular"])
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像なしでも保存できる`() {
            val shop = createShop(images = emptyList())

            shopRepositoryImpl.save(shop)

            val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
            assertEquals(1, shops.size)
            val images = jdbcTemplate.queryForList("SELECT * FROM shop_images")
            assertEquals(0, images.size)
        }
    }

    @Nested
    inner class 複数行INSERT {
        @Test
        fun `複数の画像を保存できる`() {
            val shop = createShop(
                images = listOf(
                    ShopImage(
                        id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                        type = ShopImageType.MAIN,
                        imageUrl = ImageUrl("https://example.com/shop1.png"),
                    ),
                    ShopImage(
                        id = ShopImageId("00000000-0000-4000-8000-000000000012"),
                        type = ShopImageType.MAIN,
                        imageUrl = ImageUrl("https://example.com/shop2.png"),
                    ),
                ),
            )

            shopRepositoryImpl.save(shop)

            val images = jdbcTemplate.queryForList("SELECT * FROM shop_images ORDER BY id")
            assertEquals(2, images.size)
        }
    }
}
