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
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
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

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM coffee_bean_images")
        jdbcTemplate.execute("DELETE FROM coffee_beans")
        jdbcTemplate.execute("DELETE FROM shop_images")
        jdbcTemplate.execute("DELETE FROM tastes")
        jdbcTemplate.execute("DELETE FROM shops")
    }

    // cs-api CoffeeBeanQueryMapper は shops / coffee_bean_images (MAIN) / coffee_bean_tastes / tastes
    // すべてを INNER JOIN するため、一覧に表示されるには全テーブルへのデータ投入が必要。
    private fun insertBeanWithDependencies(
        shopId: String = "00000000-0000-4000-8000-000000000031",
        shopifyShopId: String = "shop-001",
        shopName: String = "山田珈琲焙煎所",
        prefecture: String = "TOKYO",
        beanId: String = "00000000-0000-4000-8000-000000000071",
        shopifyBeanId: String = "bean-001",
        beanName: String = "エチオピア イルガチェフェ G1",
        origin: String = "エチオピア",
        roastLevel: String = "LIGHT",
        imageId: String = "00000000-0000-4000-8000-0000000000b1",
        tasteId: String = "00000000-0000-4000-8000-000000000041",
        tasteName: String = "酸味",
        tasteEvalId: String = "00000000-0000-4000-8000-0000000000c1",
        publishStatus: String = "PUBLISHED",
    ) {
        jdbcTemplate.execute(
            "INSERT INTO shops (id, shopify_shop_id, name, shop_url, prefecture, participation_status) " +
                "VALUES ('$shopId', '$shopifyShopId', '$shopName', 'https://example.com', '$prefecture', 'PARTICIPATING')",
        )
        jdbcTemplate.execute(
            "INSERT INTO tastes (id, name) VALUES ('$tasteId', '$tasteName')",
        )
        val cols = "id, shop_id, shopify_bean_id, name, description, origin, farm, " +
            "roast_level, processing_method, is_specialty, publish_status"
        val desc = "フルーティーな香りと明るい酸味が特徴の豆です。"
        jdbcTemplate.execute(
            "INSERT INTO coffee_beans ($cols) " +
                "VALUES ('$beanId', '$shopId', '$shopifyBeanId', '$beanName', '$desc', '$origin', '農園', " +
                "'$roastLevel', 'WASHED', TRUE, '$publishStatus')",
        )
        jdbcTemplate.execute(
            "INSERT INTO coffee_bean_images (id, coffee_bean_id, type, image_url) " +
                "VALUES ('$imageId', '$beanId', 'MAIN', 'https://example.com/images/bean.jpg')",
        )
        jdbcTemplate.execute(
            "INSERT INTO coffee_bean_tastes (id, coffee_bean_id, tastes_id, evaluation_value) " +
                "VALUES ('$tasteEvalId', '$beanId', '$tasteId', 4)",
        )
    }

    @Nested
    inner class 珈琲豆一覧取得 {
        @Test
        fun `正常に珈琲豆一覧を取得すると200が返る`() {
            insertBeanWithDependencies()
            val response = coffeeBeanController.listCoffeeBeans(
                page = 0, size = 20, origin = null, roastLevel = null, prefecture = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `レスポンスボディの珈琲豆フィールドが正しい`() {
            insertBeanWithDependencies()
            val response = coffeeBeanController.listCoffeeBeans(
                page = 0, size = 20, origin = null, roastLevel = null, prefecture = null,
            )

            val item = response.body?.items?.get(0)
            assertEquals("00000000-0000-4000-8000-000000000071", item?.id)
            assertEquals("エチオピア イルガチェフェ G1", item?.name)
            assertEquals("エチオピア", item?.origin)
            assertEquals("LIGHT", item?.roastLevel)
            assertEquals(ProcessingMethod.WASHED, item?.processingMethod)
            assertEquals(true, item?.isSpecialty)
        }

        @Test
        fun `originフィルタで一致する珈琲豆のみ返る`() {
            insertBeanWithDependencies(
                shopifyBeanId = "bean-001", beanId = "00000000-0000-4000-8000-000000000071",
                origin = "エチオピア", imageId = "00000000-0000-4000-8000-0000000000b1",
                tasteId = "00000000-0000-4000-8000-000000000041", tasteEvalId = "00000000-0000-4000-8000-0000000000c1",
            )
            insertBeanWithDependencies(
                shopId = "00000000-0000-4000-8000-000000000032", shopifyShopId = "shop-002",
                beanId = "00000000-0000-4000-8000-000000000072", shopifyBeanId = "bean-002",
                beanName = "ブラジル サントス", origin = "ブラジル",
                imageId = "00000000-0000-4000-8000-0000000000b2",
                tasteId = "00000000-0000-4000-8000-000000000042", tasteName = "苦味",
                tasteEvalId = "00000000-0000-4000-8000-0000000000c2",
            )

            val response = coffeeBeanController.listCoffeeBeans(
                page = 0, size = 20, origin = "エチオピア", roastLevel = null, prefecture = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals("エチオピア イルガチェフェ G1", response.body?.items?.first()?.name)
        }

        @Test
        fun `roastLevelフィルタで一致する珈琲豆のみ返る`() {
            insertBeanWithDependencies(
                shopifyBeanId = "bean-001", beanId = "00000000-0000-4000-8000-000000000071",
                roastLevel = "LIGHT", imageId = "00000000-0000-4000-8000-0000000000b1",
                tasteId = "00000000-0000-4000-8000-000000000041", tasteEvalId = "00000000-0000-4000-8000-0000000000c1",
            )
            insertBeanWithDependencies(
                shopId = "00000000-0000-4000-8000-000000000032", shopifyShopId = "shop-002",
                beanId = "00000000-0000-4000-8000-000000000072", shopifyBeanId = "bean-002",
                beanName = "ブラジル サントス", roastLevel = "FRENCH",
                imageId = "00000000-0000-4000-8000-0000000000b2",
                tasteId = "00000000-0000-4000-8000-000000000042", tasteName = "苦味",
                tasteEvalId = "00000000-0000-4000-8000-0000000000c2",
            )

            val response = coffeeBeanController.listCoffeeBeans(
                page = 0, size = 20, origin = null,
                roastLevel = com.mametosho.domain.model.coffeebean.RoastLevel.LIGHT,
                prefecture = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals("LIGHT", response.body?.items?.first()?.roastLevel)
        }

        @Test
        fun `prefectureフィルタで一致する珈琲豆のみ返る`() {
            insertBeanWithDependencies(
                shopId = "00000000-0000-4000-8000-000000000031", shopifyShopId = "shop-001",
                prefecture = "TOKYO", beanId = "00000000-0000-4000-8000-000000000071", shopifyBeanId = "bean-001",
                imageId = "00000000-0000-4000-8000-0000000000b1",
                tasteId = "00000000-0000-4000-8000-000000000041", tasteEvalId = "00000000-0000-4000-8000-0000000000c1",
            )
            insertBeanWithDependencies(
                shopId = "00000000-0000-4000-8000-000000000032", shopifyShopId = "shop-002",
                prefecture = "OSAKA", shopName = "大阪珈琲店",
                beanId = "00000000-0000-4000-8000-000000000072", shopifyBeanId = "bean-002",
                beanName = "ブラジル サントス",
                imageId = "00000000-0000-4000-8000-0000000000b2",
                tasteId = "00000000-0000-4000-8000-000000000042", tasteName = "苦味",
                tasteEvalId = "00000000-0000-4000-8000-0000000000c2",
            )

            val response = coffeeBeanController.listCoffeeBeans(
                page = 0, size = 20, origin = null, roastLevel = null,
                prefecture = com.mametosho.domain.model.shop.Prefecture.TOKYO,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals("エチオピア イルガチェフェ G1", response.body?.items?.first()?.name)
        }

        @Test
        fun `下書き状態の珈琲豆は一覧に含まれない`() {
            insertBeanWithDependencies(
                shopId = "00000000-0000-4000-8000-000000000031", shopifyShopId = "shop-001",
                beanId = "00000000-0000-4000-8000-000000000071", shopifyBeanId = "bean-001",
                beanName = "エチオピア イルガチェフェ G1",
                imageId = "00000000-0000-4000-8000-0000000000b1",
                tasteId = "00000000-0000-4000-8000-000000000041", tasteName = "酸味",
                tasteEvalId = "00000000-0000-4000-8000-0000000000c1",
                publishStatus = "PUBLISHED",
            )
            insertBeanWithDependencies(
                shopId = "00000000-0000-4000-8000-000000000032", shopifyShopId = "shop-002",
                shopName = "下書き珈琲店",
                beanId = "00000000-0000-4000-8000-000000000072", shopifyBeanId = "bean-002",
                beanName = "下書きブラジル サントス",
                imageId = "00000000-0000-4000-8000-0000000000b2",
                tasteId = "00000000-0000-4000-8000-000000000042", tasteName = "苦味",
                tasteEvalId = "00000000-0000-4000-8000-0000000000c2",
                publishStatus = "DRAFT",
            )

            val response = coffeeBeanController.listCoffeeBeans(
                page = 0, size = 20, origin = null, roastLevel = null, prefecture = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals("00000000-0000-4000-8000-000000000071", response.body?.items?.first()?.id)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val response = coffeeBeanController.listCoffeeBeans(
                page = 0, size = 20, origin = null, roastLevel = null, prefecture = null,
            )

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }
    }
}
