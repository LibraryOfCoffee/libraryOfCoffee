package com.mametosho.cs.presentation.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
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

    private fun insertShopWithLogo(
        id: String = "00000000-0000-4000-8000-000000000031",
        shopifyShopId: String = "test-shop-001",
        name: String = "珈琲工房 まめとしょ",
        introduction: String = "東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。",
        particular: String = "厳選された豆のみを使用しています。",
        shopUrl: String = "https://mametosho.example.com",
        prefecture: String = "TOKYO",
        logoImageUrl: String = "https://example.com/logo.png",
        participationStatus: String = "PARTICIPATING",
    ) {
        jdbcTemplate.execute(
            "INSERT INTO shops " +
                "(id, shopify_shop_id, name, introduction, particular, shop_url, prefecture, participation_status) " +
                "VALUES ('$id', '$shopifyShopId', '$name', '$introduction', '$particular', " +
                "'$shopUrl', '$prefecture', '$participationStatus')",
        )
        val imageId = id.take(24) + "9" + id.drop(25)
        jdbcTemplate.execute(
            "INSERT INTO shop_images (id, shop_id, type, image_url) " +
                "VALUES ('$imageId', '$id', 'LOGO', '$logoImageUrl')",
        )
    }

    private fun insertMainImage(
        shopId: String = "00000000-0000-4000-8000-000000000031",
        imageId: String = "00000000-0000-4000-8000-000000000081",
        imageUrl: String = "https://placehold.jp/600x400.png",
    ) {
        jdbcTemplate.execute(
            "INSERT INTO shop_images (id, shop_id, type, image_url) " +
                "VALUES ('$imageId', '$shopId', 'MAIN', '$imageUrl')",
        )
    }

    @Nested
    inner class 店舗一覧取得 {
        @Test
        fun `正常に店舗一覧を取得すると200が返る`() {
            insertShopWithLogo()
            val response = shopController.listShops(page = 0, size = 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `レスポンスボディの店舗フィールドが正しい`() {
            insertShopWithLogo(
                logoImageUrl = "https://placehold.jp/100x100.png",
            )
            val response = shopController.listShops(page = 0, size = 20)

            val item = response.body?.items?.get(0)
            assertEquals("00000000-0000-4000-8000-000000000031", item?.id)
            assertEquals("珈琲工房 まめとしょ", item?.name)
            assertEquals("東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。", item?.introduction)
            assertEquals("https://mametosho.example.com", item?.shopUrl)
            assertEquals("TOKYO", item?.prefecture)
            assertEquals("https://placehold.jp/100x100.png", item?.logoImageUrl)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val response = shopController.listShops(page = 0, size = 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }

        @Test
        fun `参画前・参画落ちの店舗は一覧に含まれない`() {
            insertShopWithLogo(
                id = "00000000-0000-4000-8000-000000000031",
                shopifyShopId = "participating-shop",
                participationStatus = "PARTICIPATING",
            )
            insertShopWithLogo(
                id = "00000000-0000-4000-8000-000000000032",
                shopifyShopId = "before-shop",
                participationStatus = "BEFORE_PARTICIPATION",
            )

            val response = shopController.listShops(page = 0, size = 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            val ids = response.body?.items?.map { it.id }
            assertEquals(listOf("00000000-0000-4000-8000-000000000031"), ids)
        }

        @Test
        fun `ページネーションパラメータが正しく渡される`() {
            insertShopWithLogo(id = "00000000-0000-4000-8000-000000000031", shopifyShopId = "shop-001")
            insertShopWithLogo(id = "00000000-0000-4000-8000-000000000032", shopifyShopId = "shop-002")

            val response = shopController.listShops(page = 1, size = 1)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(2L, response.body?.totalCount)
            assertEquals(1, response.body?.page)
            assertEquals(1, response.body?.size)
        }
    }

    @Nested
    inner class 店舗詳細取得 {
        @Test
        fun `参画中の店舗を指定すると200が返る`() {
            insertShopWithLogo()
            val response = shopController.getShop("00000000-0000-4000-8000-000000000031")

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000031", response.body?.id)
        }

        @Test
        fun `レスポンスボディの店舗フィールドが正しい`() {
            insertShopWithLogo(
                logoImageUrl = "https://placehold.jp/100x100.png",
            )
            insertMainImage(imageUrl = "https://placehold.jp/600x400.png")

            val response = shopController.getShop("00000000-0000-4000-8000-000000000031")

            val body = response.body
            assertEquals("00000000-0000-4000-8000-000000000031", body?.id)
            assertEquals("珈琲工房 まめとしょ", body?.name)
            assertEquals("東京都渋谷区にある自家焙煎珈琲店。厳選されたスペシャルティコーヒーをお届けします。", body?.introduction)
            assertEquals("厳選された豆のみを使用しています。", body?.particular)
            assertEquals("https://mametosho.example.com", body?.shopUrl)
            assertEquals("TOKYO", body?.prefecture)
            assertEquals(2, body?.images?.size)
            val typeToUrl = body?.images?.associate { it.type to it.imageUrl }
            assertEquals("https://placehold.jp/100x100.png", typeToUrl?.get("LOGO"))
            assertEquals("https://placehold.jp/600x400.png", typeToUrl?.get("MAIN"))
        }

        @Test
        fun `参画前の店舗を指定すると404が返る`() {
            insertShopWithLogo(participationStatus = "BEFORE_PARTICIPATION")
            val response = shopController.getShop("00000000-0000-4000-8000-000000000031")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }

        @Test
        fun `参画落ちの店舗を指定すると404が返る`() {
            insertShopWithLogo(participationStatus = "DROPPED")
            val response = shopController.getShop("00000000-0000-4000-8000-000000000031")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }

        @Test
        fun `存在しないIDを指定すると404が返る`() {
            val response = shopController.getShop("00000000-0000-4000-8000-000000000999")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }

        @Test
        fun `UUID形式でないIDを指定するとIllegalArgumentExceptionが投げられGlobalExceptionHandlerで400に変換される`() {
            assertFailsWith<IllegalArgumentException> {
                shopController.getShop("invalid-id")
            }
        }
    }
}
