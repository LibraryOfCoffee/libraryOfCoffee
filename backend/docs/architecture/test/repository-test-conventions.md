# Repository結合テスト規約

共通ルールは [テスト共通規約](test-conventions.md) を参照。

## 概要

Repository実装クラス（`*RepositoryImpl`）のテストは、Testcontainers（MySQL）を使った結合テストとして実装する。実際のDBにINSERTし、`JdbcTemplate`で結果を検証する。

## 技術スタック

- **Testcontainers**（`org.testcontainers:mysql`, `org.testcontainers:junit-jupiter`）
- **Spring Boot Test**（`@SpringBootTest`）
- **JdbcTemplate**（INSERT結果の検証用）

```kotlin
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
```

## ファイル構成

```
common/src/test/
├── kotlin/com/mametosho/infrastructure/
│   ├── TestApplication.kt                          # テスト用Spring Bootアプリ
│   └── persistence/repository/
│       └── {Aggregate}RepositoryImplTest.kt         # 結合テスト
└── resources/
    └── application-test.yml                         # テスト用DB設定
```

## テスト基盤

### TestApplication

commonモジュールにはSpring Bootアプリケーションクラスがないため、テスト専用のものを用意する。

```kotlin
package com.mametosho.infrastructure

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@MapperScan("com.mametosho.infrastructure.persistence.mybatis.mapper")
class TestApplication
```

### application-test.yml

```yaml
spring:
  sql:
    init:
      mode: always
      schema-locations: file:../../infrastructure/db/schema.sql

mybatis:
  configuration:
    map-underscore-to-camel-case: true
```

## テストカテゴリと書くべきテスト

| カテゴリ | 書くべきテスト |
|---------|-------------|
| 正常系INSERT | 集約ルートが正しく保存されること（全カラム検証） |
| 子テーブルINSERT | 関連テーブル（images, tastesなど）が正しく保存されること |
| nullable項目 | nullable項目がnullでも保存できること |
| 空コレクション | 子テーブルのリストが空でも保存できること |
| 複数行INSERT | 子テーブルに複数行INSERTできること |
| enum変換 | enum値が`.name.lowercase()`で正しくDB保存されること（全enum値） |

## 標準実装リファレンス

以下はRepository結合テストを実装する際のテンプレートとなるコード例。

```kotlin
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
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import org.junit.jupiter.api.BeforeEach
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
        // 子テーブル → 親テーブルの順で削除（FK制約）
        jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
        jdbcTemplate.execute("DELETE FROM coffee_bean_images")
        jdbcTemplate.execute("DELETE FROM coffee_beans")
        // 外部キー参照先のテストデータを投入
        jdbcTemplate.execute("DELETE FROM tastes")
        jdbcTemplate.execute("DELETE FROM shops")
        jdbcTemplate.execute(
            """
            INSERT INTO shops (id, shopify_shop_id, name, introduction, particular)
            VALUES ('00000000-0000-4000-8000-000000000001', 'test-shop-001', 'テスト店舗', 'テスト紹介', 'テストこだわり')
            """,
        )
        jdbcTemplate.execute(
            """
            INSERT INTO tastes (id, name)
            VALUES ('00000000-0000-4000-8000-000000000101', 'テイスト1')
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
        images = images,
        tastes = tastes,
    )

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
        assertEquals("medium", beans[0]["roast_level"])
        assertEquals("washed", beans[0]["processing_method"])
        assertEquals(true, beans[0]["is_specialty"])
    }

    @Test
    fun `画像が正しく保存される`() {
        coffeeBeanRepositoryImpl.save(createCoffeeBean())

        val images = jdbcTemplate.queryForList("SELECT * FROM coffee_bean_images")
        assertEquals(1, images.size)
        assertEquals("00000000-0000-4000-8000-000000000011", images[0]["id"])
        assertEquals("00000000-0000-4000-8000-000000000010", images[0]["coffee_bean_id"])
        assertEquals("main", images[0]["type"])
        assertEquals("https://example.com/bean.png", images[0]["image_url"])
    }

    @Test
    fun `farmがnullでも保存できる`() {
        coffeeBeanRepositoryImpl.save(createCoffeeBean(farm = null))

        val beans = jdbcTemplate.queryForList("SELECT * FROM coffee_beans")
        assertEquals(1, beans.size)
        assertEquals(null, beans[0]["farm"])
    }

    @Test
    fun `全焙煎度をlowercaseで保存できる`() {
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
                images = emptyList(),
                tastes = emptyList(),
            )
            coffeeBeanRepositoryImpl.save(coffeeBean)
        }

        val beans = jdbcTemplate.queryForList("SELECT roast_level FROM coffee_beans ORDER BY id")
        assertEquals(RoastLevel.entries.size, beans.size)
        beans.forEachIndexed { index, row ->
            assertEquals(RoastLevel.entries[index].name.lowercase(), row["roast_level"])
        }
    }
}
```

## チェックリスト

新しいRepositoryのテストを実装するときに確認すること:

- [ ] `@SpringBootTest(classes = [TestApplication::class])` を指定しているか
- [ ] `@Testcontainers` と `@ActiveProfiles("test")` を付与しているか
- [ ] `companion object` 内でMySQLContainerとDynamicPropertySourceを定義しているか
- [ ] `@BeforeEach` で子テーブル→親テーブルの順にDELETEしているか
- [ ] FK参照先のテストデータを `@BeforeEach` で投入しているか
- [ ] ファクトリメソッドを使ってドメインモデルを生成しているか
- [ ] 全カラムの保存値を検証しているか
- [ ] enum値の`.name.lowercase()`変換を全enum値で検証しているか
- [ ] nullable項目・空コレクションのテストがあるか
