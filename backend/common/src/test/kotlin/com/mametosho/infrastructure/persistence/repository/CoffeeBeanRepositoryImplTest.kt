package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImage
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageType
import com.mametosho.domain.model.coffeebean.CoffeeBeanTaste
import com.mametosho.domain.model.coffeebean.CoffeeBeanTasteId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shared.PublishStatus
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import java.util.Locale
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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
import kotlin.test.assertTrue

@SpringBootTest(classes = [com.mametosho.infrastructure.TestApplication::class])
@Testcontainers
@ActiveProfiles("test")
class CoffeeBeanRepositoryImplTest {

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
    private lateinit var coffeeBeanRepositoryImpl: CoffeeBeanRepositoryImpl

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM coffee_bean_images")
        jdbcTemplate.execute("DELETE FROM coffee_beans")
        jdbcTemplate.execute("DELETE FROM tastes")
        jdbcTemplate.execute("DELETE FROM shops")

        jdbcTemplate.execute(
            """
            INSERT INTO shops (id, shopify_shop_id, name, introduction, particular, shop_url, prefecture)
            VALUES ('00000000-0000-4000-8000-000000000001', 'test-shop-001', 'テスト店舗', 'テスト紹介', 'テストこだわり', 'https://example.com', 'TOKYO')
            """,
        )
        jdbcTemplate.execute(
            """
            INSERT INTO tastes (id, name)
            VALUES ('00000000-0000-4000-8000-000000000101', 'テイスト1')
            """,
        )
        jdbcTemplate.execute(
            """
            INSERT INTO tastes (id, name)
            VALUES ('00000000-0000-4000-8000-000000000102', 'テイスト2')
            """,
        )
    }

    private fun createCoffeeBean(
        id: String = "00000000-0000-4000-8000-000000000010",
        shopifyBeanId: String = "test-bean-001",
        farm: String? = "テスト農園",
        images: List<CoffeeBeanImage> = listOf(
            CoffeeBeanImage(
                id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000011"),
                type = CoffeeBeanImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/bean.png"),
            ),
        ),
        tastes: List<CoffeeBeanTaste> = listOf(
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000021"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000101"),
                evaluationValue = 3,
            ),
        ),
    ): CoffeeBean = CoffeeBean(
        id = CoffeeBeanId(id),
        shopId = ShopId("00000000-0000-4000-8000-000000000001"),
        shopifyBeanId = ShopifyBeanId(shopifyBeanId),
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = farm,
        roastLevel = RoastLevel.MEDIUM,
        processingMethod = ProcessingMethod.WASHED,
        isSpecialty = true,
        publishStatus = PublishStatus.PUBLISHED,
        images = images,
        tastes = tastes,
    )

    @Nested
    inner class 正常系INSERT {
        @Test
        fun `正常にCoffeeBeanを保存できる`() {
            val coffeeBean = createCoffeeBean()

            coffeeBeanRepositoryImpl.save(coffeeBean)

            val beans = jdbcTemplate.queryForList("SELECT * FROM coffee_beans")
            assertEquals(1, beans.size)
            assertEquals("00000000-0000-4000-8000-000000000010", beans[0]["id"])
            assertEquals("00000000-0000-4000-8000-000000000001", beans[0]["shop_id"])
            assertEquals("test-bean-001", beans[0]["shopify_bean_id"])
            assertEquals("テストコーヒー豆", beans[0]["name"])
            assertEquals("テスト説明文", beans[0]["description"])
            assertEquals("エチオピア", beans[0]["origin"])
            assertEquals("テスト農園", beans[0]["farm"])
            assertEquals("MEDIUM", beans[0]["roast_level"])
            assertEquals("WASHED", beans[0]["processing_method"])
            assertEquals(true, beans[0]["is_specialty"])
        }
    }

    @Nested
    inner class 子テーブルINSERT {
        @Test
        fun `画像が正しく保存される`() {
            val coffeeBean = createCoffeeBean()

            coffeeBeanRepositoryImpl.save(coffeeBean)

            val images = jdbcTemplate.queryForList("SELECT * FROM coffee_bean_images")
            assertEquals(1, images.size)
            assertEquals("00000000-0000-4000-8000-000000000011", images[0]["id"])
            assertEquals("00000000-0000-4000-8000-000000000010", images[0]["coffee_bean_id"])
            assertEquals("MAIN", images[0]["type"])
            assertEquals("https://example.com/bean.png", images[0]["image_url"])
        }

        @Test
        fun `テイスト評価が正しく保存される`() {
            val coffeeBean = createCoffeeBean()

            coffeeBeanRepositoryImpl.save(coffeeBean)

            val tastes = jdbcTemplate.queryForList("SELECT * FROM coffee_bean_tastes")
            assertEquals(1, tastes.size)
            assertEquals("00000000-0000-4000-8000-000000000021", tastes[0]["id"])
            assertEquals("00000000-0000-4000-8000-000000000010", tastes[0]["coffee_bean_id"])
            assertEquals("00000000-0000-4000-8000-000000000101", tastes[0]["tastes_id"])
            assertEquals(3, tastes[0]["evaluation_value"])
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `farmがnullでも保存できる`() {
            val coffeeBean = createCoffeeBean(farm = null)

            coffeeBeanRepositoryImpl.save(coffeeBean)

            val beans = jdbcTemplate.queryForList("SELECT * FROM coffee_beans")
            assertEquals(1, beans.size)
            assertEquals(null, beans[0]["farm"])
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像が空の場合はIllegalArgumentExceptionが発生する`() {
            assertThrows<IllegalArgumentException> {
                createCoffeeBean(images = emptyList())
            }
        }

        @Test
        fun `テイストが空の場合はIllegalArgumentExceptionが発生する`() {
            assertThrows<IllegalArgumentException> {
                createCoffeeBean(tastes = emptyList())
            }
        }

        @Test
        fun `MAIN画像が2枚の場合はIllegalArgumentExceptionが発生する`() {
            assertThrows<IllegalArgumentException> {
                createCoffeeBean(
                    images = listOf(
                        CoffeeBeanImage(
                            id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000011"),
                            type = CoffeeBeanImageType.MAIN,
                            imageUrl = ImageUrl("https://example.com/bean1.png"),
                        ),
                        CoffeeBeanImage(
                            id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000012"),
                            type = CoffeeBeanImageType.MAIN,
                            imageUrl = ImageUrl("https://example.com/bean2.png"),
                        ),
                    ),
                )
            }
        }
    }

    @Nested
    inner class 複数行INSERT {

        @Test
        fun `複数のテイスト評価を保存できる`() {
            val coffeeBean = createCoffeeBean(
                tastes = listOf(
                    CoffeeBeanTaste(
                        id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000021"),
                        tasteId = TasteId("00000000-0000-4000-8000-000000000101"),
                        evaluationValue = 3,
                    ),
                    CoffeeBeanTaste(
                        id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000022"),
                        tasteId = TasteId("00000000-0000-4000-8000-000000000102"),
                        evaluationValue = 5,
                    ),
                ),
            )

            coffeeBeanRepositoryImpl.save(coffeeBean)

            val tastes = jdbcTemplate.queryForList("SELECT * FROM coffee_bean_tastes ORDER BY id")
            assertEquals(2, tastes.size)
        }
    }

    @Nested
    inner class enum変換 {
        @Test
        fun `全焙煎度を大文字で保存できる`() {
            RoastLevel.entries.forEachIndexed { index, roastLevel ->
                val coffeeBean = CoffeeBean(
                    id = CoffeeBeanId("00000000-0000-4000-8000-00000000100$index"),
                    shopId = ShopId("00000000-0000-4000-8000-000000000001"),
                    shopifyBeanId = ShopifyBeanId("test-bean-roast-$index"),
                    name = "テスト豆",
                    description = "テスト説明",
                    origin = "エチオピア",
                    farm = null,
                    roastLevel = roastLevel,
                    processingMethod = ProcessingMethod.WASHED,
                    isSpecialty = false,
                    publishStatus = PublishStatus.PUBLISHED,
                    images = listOf(
                        CoffeeBeanImage(
                            id = CoffeeBeanImageId("00000000-0000-4000-${String.format(Locale.ROOT, "%04d", index)}-000000000099"),
                            type = CoffeeBeanImageType.MAIN,
                            imageUrl = ImageUrl("https://example.com/bean-$index.jpg"),
                        ),
                    ),
                    tastes = listOf(
                        CoffeeBeanTaste(
                            id = CoffeeBeanTasteId("00000000-0000-4001-${String.format(Locale.ROOT, "%04d", index)}-000000000099"),
                            tasteId = TasteId("00000000-0000-4000-8000-000000000101"),
                            evaluationValue = 3,
                        ),
                    ),
                )
                coffeeBeanRepositoryImpl.save(coffeeBean)
            }

            val beans = jdbcTemplate.queryForList("SELECT roast_level FROM coffee_beans ORDER BY id")
            assertEquals(RoastLevel.entries.size, beans.size)
            beans.forEachIndexed { index, row ->
                assertEquals(RoastLevel.entries[index].name, row["roast_level"])
            }
        }

        @Test
        fun `全精製方法を大文字で保存できる`() {
            ProcessingMethod.entries.forEachIndexed { index, method ->
                val coffeeBean = CoffeeBean(
                    id = CoffeeBeanId("00000000-0000-4000-8000-00000000200$index"),
                    shopId = ShopId("00000000-0000-4000-8000-000000000001"),
                    shopifyBeanId = ShopifyBeanId("test-bean-proc-$index"),
                    name = "テスト豆",
                    description = "テスト説明",
                    origin = "エチオピア",
                    farm = null,
                    roastLevel = RoastLevel.MEDIUM,
                    processingMethod = method,
                    isSpecialty = false,
                    publishStatus = PublishStatus.PUBLISHED,
                    images = listOf(
                        CoffeeBeanImage(
                            id = CoffeeBeanImageId("00000000-0000-4000-${String.format(Locale.ROOT, "%04d", index)}-000000000099"),
                            type = CoffeeBeanImageType.MAIN,
                            imageUrl = ImageUrl("https://example.com/bean-$index.jpg"),
                        ),
                    ),
                    tastes = listOf(
                        CoffeeBeanTaste(
                            id = CoffeeBeanTasteId("00000000-0000-4001-${String.format(Locale.ROOT, "%04d", index)}-000000000099"),
                            tasteId = TasteId("00000000-0000-4000-8000-000000000101"),
                            evaluationValue = 3,
                        ),
                    ),
                )
                coffeeBeanRepositoryImpl.save(coffeeBean)
            }

            val beans = jdbcTemplate.queryForList("SELECT processing_method FROM coffee_beans ORDER BY id")
            assertEquals(ProcessingMethod.entries.size, beans.size)
            beans.forEachIndexed { index, row ->
                assertEquals(ProcessingMethod.entries[index].name, row["processing_method"].toString())
            }
        }
    }
}
