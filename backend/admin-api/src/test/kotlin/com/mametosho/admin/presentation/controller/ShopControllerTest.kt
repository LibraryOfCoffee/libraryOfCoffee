package com.mametosho.admin.presentation.controller

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.admin.presentation.dto.request.UpdateShopRequest
import com.mametosho.admin.test.FakeImageStorageConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(FakeImageStorageConfig::class)
class ShopControllerTest {

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
    private lateinit var shopController: ShopController

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM coffee_bean_images")
        jdbcTemplate.execute("DELETE FROM coffee_beans")
        jdbcTemplate.execute("DELETE FROM shop_images")
        jdbcTemplate.execute("DELETE FROM shops")
    }

    // Shop ドメインは LOGO 画像が必ず 1 枚必要なため、挿入時に shop_images へも登録する。
    // 画像IDは shop_id の先頭セグメントを "9" に置き換えて一意性を確保する。
    private fun insertShop(
        id: String = "00000000-0000-4000-8000-000000000001",
        shopifyShopId: String = "test-shop-001",
        name: String = "テスト店舗",
        prefecture: String = "TOKYO",
    ) {
        jdbcTemplate.execute(
            "INSERT INTO shops (id, shopify_shop_id, name, shop_url, prefecture) " +
                "VALUES ('$id', '$shopifyShopId', '$name', 'https://example.com', '$prefecture')",
        )
        val imageId = id.take(24) + "9" + id.drop(25)
        jdbcTemplate.execute(
            "INSERT INTO shop_images (id, shop_id, type, image_url) " +
                "VALUES ('$imageId', '$id', 'LOGO', 'https://example.com/logo.png')",
        )
    }

    private val logoFile = MockMultipartFile("images", "logo.png", "image/png", byteArrayOf(1))

    @Nested
    inner class 店舗一覧取得 {
        @Test
        fun `正常に店舗一覧を取得すると200が返る`() {
            insertShop()
            val response = shopController.listShops(0, 20, null)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val response = shopController.listShops(0, 20, null)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }

        @Test
        fun `店名で絞り込みすると一致する店舗のみ返る`() {
            insertShop(id = "00000000-0000-4000-8000-000000000001", shopifyShopId = "shop-001", name = "テスト店舗")
            insertShop(id = "00000000-0000-4000-8000-000000000002", shopifyShopId = "shop-002", name = "別の珈琲店")

            val response = shopController.listShops(0, 20, "テスト")

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals("テスト店舗", response.body?.items?.first()?.name)
        }

        @Test
        fun `店名検索で結果が0件の場合も200が返る`() {
            val response = shopController.listShops(0, 20, "存在しない店舗")

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }
    }

    @Nested
    inner class 店舗登録 {
        @Test
        fun `正常に店舗を登録すると201が返る`() {
            val request = CreateShopRequest(
                shopifyShopId = "test-shop-001",
                name = "テスト店舗",
                introduction = "テスト紹介文",
                particular = "テストこだわり",
                shopUrl = "https://example.com",
                prefecture = "TOKYO",
                publishStatus = "PUBLISHED",
            )

            val response = shopController.createShop(request, listOf(logoFile), listOf("LOGO"))

            assertEquals(HttpStatus.CREATED, response.statusCode)
            val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
            assertEquals(1, shops.size)
            assertEquals("test-shop-001", shops[0]["shopify_shop_id"])
        }
    }

    @Nested
    inner class 店舗編集 {
        @Test
        fun `正常に店舗を編集すると200が返る`() {
            insertShop()
            val request = UpdateShopRequest(
                shopifyShopId = "test-shop-001",
                name = "更新店舗",
                introduction = "更新紹介文",
                particular = "更新こだわり",
                shopUrl = "https://updated.example.com",
                prefecture = "OSAKA",
                publishStatus = "PUBLISHED",
            )

            val response = shopController.updateShop("00000000-0000-4000-8000-000000000001", request, listOf(logoFile), listOf("LOGO"))

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        }

        @Test
        fun `存在しない店舗を編集すると404が返る`() {
            val request = UpdateShopRequest(
                shopifyShopId = "not-exist",
                name = "存在しない店舗",
                introduction = null,
                particular = null,
                shopUrl = "https://example.com",
                prefecture = "TOKYO",
                publishStatus = "PUBLISHED",
            )

            val response = shopController.updateShop("00000000-0000-4000-8000-999999999999", request, listOf(logoFile), listOf("LOGO"))

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertNull(response.body)
        }
    }

    @Nested
    inner class 店舗削除 {
        @Test
        fun `正常に店舗を削除すると204が返る`() {
            insertShop()

            val response = shopController.deleteShop("00000000-0000-4000-8000-000000000001")

            assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
            assertNull(response.body)
            val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
            assertEquals(0, shops.size)
        }

        @Test
        fun `存在しない店舗を削除すると404が返る`() {
            val response = shopController.deleteShop("00000000-0000-4000-8000-999999999999")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertNull(response.body)
        }
    }
}
