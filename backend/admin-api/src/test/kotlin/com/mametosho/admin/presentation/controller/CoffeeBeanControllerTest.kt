package com.mametosho.admin.presentation.controller

import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.admin.presentation.dto.request.UpdateCoffeeBeanRequest
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
class CoffeeBeanControllerTest {

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
    private lateinit var coffeeBeanController: CoffeeBeanController

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val tasteId = "00000000-0000-4000-8000-000000000041"

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM coffee_bean_images")
        jdbcTemplate.execute("DELETE FROM coffee_beans")
        jdbcTemplate.execute("DELETE FROM shop_images")
        jdbcTemplate.execute("DELETE FROM tastes")
        jdbcTemplate.execute("DELETE FROM shops")
    }

    private fun insertShop(id: String = "00000000-0000-4000-8000-000000000001") {
        jdbcTemplate.execute(
            "INSERT INTO shops (id, shopify_shop_id, name, shop_url, prefecture) " +
                "VALUES ('$id', 'test-shop-001', 'テスト店舗', 'https://example.com', 'TOKYO')",
        )
    }

    // CoffeeBean ドメインは MAIN 画像とテイストがそれぞれ 1 件以上必要なため、
    // 挿入時に coffee_bean_images / tastes / coffee_bean_tastes へも登録する。
    // 画像IDは bean_id の先頭セグメントを "9" に、テイスト評価IDを "7" に置き換えて一意性を確保する。
    // テイストIDは固定値を使い、create/update リクエストのテイスト参照と合わせる。
    private fun insertCoffeeBean(
        id: String = "00000000-0000-4000-8000-000000000071",
        shopId: String = "00000000-0000-4000-8000-000000000001",
        shopifyBeanId: String = "test-bean-001",
    ) {
        val cols = "id, shop_id, shopify_bean_id, name, description, origin, farm, roast_level, processing_method, is_specialty"
        jdbcTemplate.execute(
            "INSERT INTO coffee_beans ($cols) " +
                "VALUES ('$id', '$shopId', '$shopifyBeanId', 'テストコーヒー豆', 'テスト説明文', 'エチオピア', 'テスト農園', 'MEDIUM', 'WASHED', TRUE)",
        )
        val imageId = id.take(24) + "9" + id.drop(25)
        jdbcTemplate.execute(
            "INSERT INTO coffee_bean_images (id, coffee_bean_id, type, image_url) " +
                "VALUES ('$imageId', '$id', 'MAIN', 'https://example.com/bean.jpg')",
        )
        jdbcTemplate.execute(
            "INSERT IGNORE INTO tastes (id, name) VALUES ('$tasteId', '酸味')",
        )
        val tasteEvalId = id.take(24) + "7" + id.drop(25)
        jdbcTemplate.execute(
            "INSERT INTO coffee_bean_tastes (id, coffee_bean_id, tastes_id, evaluation_value) " +
                "VALUES ('$tasteEvalId', '$id', '$tasteId', 4)",
        )
    }

    private val mainImage = MockMultipartFile("images", "main.jpg", "image/jpeg", byteArrayOf(1))

    private fun createRequest(shopId: String = "00000000-0000-4000-8000-000000000001") = CreateCoffeeBeanRequest(
        shopId = shopId,
        shopifyBeanId = "test-bean-001",
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = "テスト農園",
        roastLevel = "MEDIUM",
        processingMethod = "WASHED",
        isSpecialty = true,
        tastes = listOf(CreateCoffeeBeanRequest.TasteRequest(tasteId = tasteId, evaluationValue = 4)),
    )

    private fun createUpdateRequest(shopId: String = "00000000-0000-4000-8000-000000000001") = UpdateCoffeeBeanRequest(
        shopId = shopId,
        shopifyBeanId = "test-bean-001",
        name = "更新コーヒー豆",
        description = "更新説明文",
        origin = "ブラジル",
        farm = "更新農園",
        roastLevel = "FRENCH",
        processingMethod = "NATURAL",
        isSpecialty = true,
        tastes = listOf(UpdateCoffeeBeanRequest.TasteRequest(tasteId = tasteId, evaluationValue = 3)),
    )

    @Nested
    inner class コーヒー豆一覧取得 {
        @Test
        fun `正常にコーヒー豆一覧を取得すると200が返る`() {
            insertShop()
            insertCoffeeBean()
            val response = coffeeBeanController.listCoffeeBeans(0, 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val response = coffeeBeanController.listCoffeeBeans(0, 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }
    }

    @Nested
    inner class コーヒー豆詳細取得 {
        @Test
        fun `正常にコーヒー豆詳細を取得すると200が返る`() {
            insertShop()
            insertCoffeeBean()
            val response = coffeeBeanController.getCoffeeBean("00000000-0000-4000-8000-000000000071")

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000071", response.body?.id)
            assertEquals("テストコーヒー豆", response.body?.name)
        }

        @Test
        fun `存在しないコーヒー豆を取得すると404が返る`() {
            val response = coffeeBeanController.getCoffeeBean("00000000-0000-4000-8000-999999999999")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }
    }

    @Nested
    inner class コーヒー豆登録 {
        @Test
        fun `正常にコーヒー豆を登録すると201が返る`() {
            insertShop()
            jdbcTemplate.execute("INSERT INTO tastes (id, name) VALUES ('$tasteId', '酸味')")

            val response = coffeeBeanController.createCoffeeBean(createRequest(), listOf(mainImage), listOf("MAIN"))

            assertEquals(HttpStatus.CREATED, response.statusCode)
            val beans = jdbcTemplate.queryForList("SELECT * FROM coffee_beans")
            assertEquals(1, beans.size)
            assertEquals("テストコーヒー豆", beans[0]["name"])
        }
    }

    @Nested
    inner class コーヒー豆更新 {
        @Test
        fun `正常にコーヒー豆を更新すると200が返る`() {
            insertShop()
            insertCoffeeBean()

            val response = coffeeBeanController.updateCoffeeBean(
                "00000000-0000-4000-8000-000000000071",
                createUpdateRequest(),
                emptyList(),
                emptyList(),
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000071", response.body?.id)
        }

        @Test
        fun `存在しないコーヒー豆を更新すると404が返る`() {
            val response = coffeeBeanController.updateCoffeeBean(
                "00000000-0000-4000-8000-999999999999",
                createUpdateRequest(),
                emptyList(),
                emptyList(),
            )

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertNull(response.body)
        }
    }

    @Nested
    inner class コーヒー豆削除 {
        @Test
        fun `正常にコーヒー豆を削除すると204が返る`() {
            insertShop()
            insertCoffeeBean()

            val response = coffeeBeanController.deleteCoffeeBean("00000000-0000-4000-8000-000000000071")

            assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
            assertNull(response.body)
            val beans = jdbcTemplate.queryForList("SELECT * FROM coffee_beans")
            assertEquals(0, beans.size)
        }

        @Test
        fun `存在しないコーヒー豆を削除すると404が返る`() {
            val response = coffeeBeanController.deleteCoffeeBean("00000000-0000-4000-8000-999999999999")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertNull(response.body)
        }
    }
}
