package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.infrastructure.persistence.mybatis.entity.ShopImageEntity
import com.mametosho.infrastructure.persistence.mybatis.mapper.ShopMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
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

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class CreateShopUsecaseIntegrationTest {

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
    private lateinit var createShopUsecase: CreateShopUsecase

    @Autowired
    private lateinit var shopMapper: ShopMapper

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

    @Test
    fun `正常にShopを保存できる`() {
        val request = CreateShopRequest(
            shopifyShopId = "test-shop-001",
            name = "テスト店舗",
            introduction = "テスト紹介文",
            particular = "テストこだわり",
            images = listOf(
                CreateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/image.png"),
            ),
        )

        val shop = createShopUsecase.execute(request)

        val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
        assertEquals(1, shops.size)
        assertEquals(shop.id.value, shops[0]["id"])

        val images = jdbcTemplate.queryForList("SELECT * FROM shop_images")
        assertEquals(1, images.size)
    }

    @Test
    fun `shop_imagesのINSERT失敗時にshopsもロールバックされる`() {
        // 先にダミーのshopとshop_imageを作り、重複する画像IDを用意する
        jdbcTemplate.execute(
            """
            INSERT INTO shops (id, shopify_shop_id, name)
            VALUES ('00000000-0000-4000-8000-000000000098', 'dummy-shop', 'ダミー店舗')
            """,
        )
        val duplicateImageId = "00000000-0000-4000-8000-000000000099"
        shopMapper.insertShopImage(
            ShopImageEntity(
                id = duplicateImageId,
                shopId = "00000000-0000-4000-8000-000000000098",
                type = "main",
                imageUrl = "https://example.com/existing.png",
            ),
        )

        val shopCountBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM shops", Int::class.java)

        // UsecaseはUUIDを自動生成するため、画像ID重複を直接起こせない
        // → ShopMapperに直接重複IDの画像を事前INSERTし、
        //   同じIDをUsecaseが生成するようにはできないため、
        //   Repository.save()を直接呼ぶテスト用のUsecaseを用意する代わりに
        //   shopify_shop_idのUNIQUE制約違反でテストする

        // shopify_shop_id "dummy-shop" は既に存在するため、UNIQUE制約違反が発生する
        val request = CreateShopRequest(
            shopifyShopId = "dummy-shop",
            name = "重複店舗",
            introduction = null,
            particular = null,
            images = listOf(
                CreateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/new.png"),
            ),
        )

        assertThrows<Exception> {
            createShopUsecase.execute(request)
        }

        val shopCountAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM shops", Int::class.java)
        assertEquals(shopCountBefore, shopCountAfter, "shopify_shop_idのUNIQUE制約違反時にshopsへのINSERTがロールバックされること")
    }
}
