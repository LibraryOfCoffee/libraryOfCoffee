# コントローラテスト規約

共通ルールは [テスト共通規約](test-conventions.md) を参照。

## 概要

コントローラ（`*Controller`）のテストは、Testcontainers（MySQL）を使った統合テストとして実装する。Spring コンテキスト全体を起動し、実DBを通してHTTPステータスコードとレスポンスボディを検証する。

## ファイル構成

```
admin-api/src/test/kotlin/com/mametosho/admin/presentation/controller/
└── {Aggregate}ControllerTest.kt

cs-api/src/test/kotlin/com/mametosho/cs/presentation/controller/
└── {Aggregate}ControllerTest.kt
```

## テストカテゴリと書くべきテスト

テストはエンドポイントごとに `@Nested inner class` でグルーピングする。ネストクラス名はエンドポイントの操作名（例: `店舗登録`, `店舗編集`, `店舗削除`）とする。

各エンドポイントのネストクラス内に、正常系・異常系（404など）のテストをまとめて記述する。

| 書くべきテスト | 例 |
|-------------|---|
| 正常系で期待するHTTPステータスが返ること | `正常に店舗を登録すると201が返る` |
| 異常系で期待するHTTPステータスが返ること | `存在しない店舗を編集すると404が返る` |
| レスポンスボディの主要フィールドが正しいこと | ステータスコード検証と同じテスト内で検証 |

## Testcontainers によるデータベース設定

MySQL コンテナを `companion object` 内で `@Container @JvmStatic` として宣言し、`@DynamicPropertySource` でデータソース設定を注入する。

```kotlin
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
```

## ImageStorageService の差し替え（画像アップロードを伴うコントローラのみ）

`ImageStorageService`（S3）に依存するコントローラをテストする場合は、`@TestConfiguration` でフェイク実装を差し込む。

```kotlin
@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun imageStorageService(): ImageStorageService = FakeImageStorageService
}
```

## BeforeEach によるDBクリーンアップ

各テストの前に関連テーブルを外部キー制約の依存順（子テーブル→親テーブル）でクリーンアップする。

```kotlin
@BeforeEach
fun setUp() {
    jdbcTemplate.execute("DELETE FROM coffee_bean_tastes")
    jdbcTemplate.execute("DELETE FROM coffee_bean_images")
    jdbcTemplate.execute("DELETE FROM coffee_beans")
    jdbcTemplate.execute("DELETE FROM shop_images")
    jdbcTemplate.execute("DELETE FROM shops")
}
```

## テストデータの投入

`JdbcTemplate` で直接SQLを実行してテストデータを用意する。ファクトリメソッド（`private fun insertXxx(...)`）にまとめて再利用する。

```kotlin
private fun insertShop(
    id: String = "00000000-0000-4000-8000-000000000001",
    name: String = "テスト店舗",
) {
    jdbcTemplate.execute(
        "INSERT INTO shops (id, shopify_shop_id, name, shop_url, prefecture) " +
            "VALUES ('$id', 'test-shop-001', '$name', 'https://example.com', 'TOKYO')"
    )
}
```

## 標準実装リファレンス

以下はコントローラテストを実装する際のテンプレートとなるコード例。

```kotlin
package com.mametosho.admin.presentation.controller

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.admin.test.FakeImageStorageService
import com.mametosho.domain.service.ImageStorageService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
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
class ShopControllerTest {

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
    private lateinit var shopController: ShopController

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM shop_images")
        jdbcTemplate.execute("DELETE FROM shops")
    }

    private fun insertShop(id: String = "00000000-0000-4000-8000-000000000001") {
        jdbcTemplate.execute(
            "INSERT INTO shops (id, shopify_shop_id, name, shop_url, prefecture) " +
                "VALUES ('$id', 'test-shop-001', 'テスト店舗', 'https://example.com', 'TOKYO')"
        )
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
            )

            val response = shopController.createShop(request, null, null)

            assertEquals(HttpStatus.CREATED, response.statusCode)
            val shops = jdbcTemplate.queryForList("SELECT * FROM shops")
            assertEquals(1, shops.size)
        }
    }
}
```

## チェックリスト

新しいコントローラテストを実装するときに確認すること:

- [ ] `@SpringBootTest`, `@Testcontainers`, `@ActiveProfiles("test")` が付いているか
- [ ] MySQL Testcontainer が `companion object` に `@Container @JvmStatic` で宣言されているか
- [ ] `@DynamicPropertySource` でデータソース設定が注入されているか
- [ ] `ImageStorageService` を持つコントローラでは `@TestConfiguration` で差し替えているか
- [ ] `@BeforeEach` で外部キー順にテーブルがクリーンアップされているか
- [ ] テストデータ投入に `private fun insertXxx(...)` ファクトリメソッドを使っているか
- [ ] テストがエンドポイントごとに `@Nested inner class` でグルーピングされているか
- [ ] HTTPステータスコードを検証しているか
- [ ] レスポンスボディの主要フィールドを検証しているか
