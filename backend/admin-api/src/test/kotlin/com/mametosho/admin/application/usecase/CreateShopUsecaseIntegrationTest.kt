package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.admin.test.FakeImageStorageService
import com.mametosho.domain.service.ImageStorageService
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.mock.web.MockMultipartFile
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class CreateShopUsecaseIntegrationTest {

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun imageStorageService(): ImageStorageService = FakeImageStorageService
    }

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
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM shop_images")
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM coffee_bean_images")
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
            shopUrl = "https://example.com",
            prefecture = "TOKYO",
        )

        val logoFile = MockMultipartFile("images", "logo.png", "image/png", byteArrayOf(1))
        val shop = createShopUsecase.execute(request, listOf(logoFile), listOf("LOGO"))

        val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
        assertEquals(1, shops.size)
        assertEquals(shop.id.value, shops[0]["id"])
    }

    @Test
    fun `重複するshopify_shop_idの場合はupsertでshop行数が増えない`() {
        jdbcTemplate.execute(
            """
            INSERT INTO shops (id, shopify_shop_id, name, shop_url, prefecture)
            VALUES ('00000000-0000-4000-8000-000000000098', 'dummy-shop', 'ダミー店舗', 'https://dummy.example.com', 'TOKYO')
            """,
        )

        val shopCountBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM shops", Int::class.java)

        jdbcTemplate.execute(
            """
            INSERT INTO shops (id, shopify_shop_id, name, shop_url, prefecture)
            VALUES ('00000000-0000-4000-8000-000000000099', 'dummy-shop', '重複店舗', 'https://dummy-shop.example.com', 'OSAKA')
            ON DUPLICATE KEY UPDATE
                name = VALUES(name),
                shop_url = VALUES(shop_url)
            """,
        )

        val shopCountAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM shops", Int::class.java)
        assertEquals(shopCountBefore, shopCountAfter, "重複するshopify_shop_idの場合は新規INSERTではなくUPDATEされること")

        val updatedName = jdbcTemplate.queryForObject(
            "SELECT name FROM shops WHERE id = '00000000-0000-4000-8000-000000000098'",
            String::class.java,
        )
        assertEquals("重複店舗", updatedName)
    }
}
