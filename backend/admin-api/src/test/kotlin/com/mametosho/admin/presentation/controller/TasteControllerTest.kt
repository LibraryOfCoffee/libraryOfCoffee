package com.mametosho.admin.presentation.controller

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
import kotlin.test.assertNotNull

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class TasteControllerTest {

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
    private lateinit var tasteController: TasteController

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM tastes")
    }

    @Nested
    inner class テイスト一覧取得 {
        @Test
        fun `正常にテイスト一覧を取得すると200が返る`() {
            jdbcTemplate.execute("INSERT INTO tastes (id, name) VALUES ('00000000-0000-4000-8000-000000000041', '酸味')")
            jdbcTemplate.execute("INSERT INTO tastes (id, name) VALUES ('00000000-0000-4000-8000-000000000042', '苦味')")
            jdbcTemplate.execute("INSERT INTO tastes (id, name) VALUES ('00000000-0000-4000-8000-000000000043', '甘味')")
            jdbcTemplate.execute("INSERT INTO tastes (id, name) VALUES ('00000000-0000-4000-8000-000000000044', 'コク')")
            jdbcTemplate.execute("INSERT INTO tastes (id, name) VALUES ('00000000-0000-4000-8000-000000000045', '香り')")

            val response = tasteController.listTastes()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(5, response.body?.size)
            val sansomi = response.body?.find { it.id == "00000000-0000-4000-8000-000000000041" }
            assertNotNull(sansomi)
            assertEquals("酸味", sansomi.name)
        }

        @Test
        fun `テイストが0件の場合も200が返る`() {
            val response = tasteController.listTastes()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.size)
        }
    }
}
