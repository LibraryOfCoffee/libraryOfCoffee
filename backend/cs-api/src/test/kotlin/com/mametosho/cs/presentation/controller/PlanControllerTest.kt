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

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PlanControllerTest {

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
    private lateinit var planController: PlanController

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM plans")
    }

    @Nested
    inner class プラン一覧取得 {
        @Test
        fun `正常にプラン一覧を取得すると200が返る`() {
            jdbcTemplate.execute(
                "INSERT INTO plans (id, shopify_plan_id, label, gram_weight, bean_quantity, price, type, is_recommended) " +
                    "VALUES ('00000000-0000-4000-8000-000000000024', 'shopify-plan-1', '定番', 30, 4, 1950, 'SUBSCRIPTION', TRUE)",
            )
            val response = planController.listPlans()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.size)
        }

        @Test
        fun `レスポンスボディのプランフィールドが正しい`() {
            jdbcTemplate.execute(
                "INSERT INTO plans (id, shopify_plan_id, label, gram_weight, bean_quantity, price, type, is_recommended) " +
                    "VALUES ('00000000-0000-4000-8000-000000000024', 'shopify-plan-1', '定番', 30, 4, 1950, 'SUBSCRIPTION', TRUE)",
            )
            val response = planController.listPlans()

            val item = response.body?.get(0)
            assertEquals("00000000-0000-4000-8000-000000000024", item?.id)
            assertEquals("shopify-plan-1", item?.shopifyPlanId)
            assertEquals("定番", item?.label)
            assertEquals(30, item?.gramWeight)
            assertEquals(4, item?.beanQuantity)
            assertEquals(1950, item?.price)
            assertEquals("SUBSCRIPTION", item?.type)
            assertEquals(true, item?.isRecommended)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val response = planController.listPlans()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.size)
        }
    }
}
