# ユースケーステスト規約

共通ルールは [テスト共通規約](test-conventions.md) を参照。

## 概要

ユースケース（`*Usecase`）のテストは、Repositoryをフェイク実装に差し替えた単体テストとして実装する。ドメインモデル生成・UUID自動生成・enum変換・リポジトリ保存を検証する。

## ファイル構成

```
admin-api/src/test/kotlin/com/mametosho/admin/application/usecase/
└── {Action}{Aggregate}UsecaseTest.kt
```

## テストカテゴリと書くべきテスト

テストはカテゴリごとに `@Nested inner class` でグルーピングする。

| カテゴリ | ネストクラス名 | 書くべきテスト |
|---------|--------------|-------------|
| 正常系 | `正常系` | リクエストからドメインモデルが正しく生成されること（全フィールド検証） |
| UUID自動生成 | `UUID自動生成` | 集約ルート・子エンティティのIDがUUID形式で自動生成されること |
| nullable項目 | `nullable項目` | nullable項目がnullでも実行できること |
| 空コレクション | `空コレクション` | 子エンティティのリストが空でも実行できること |
| リポジトリ保存 | `リポジトリ保存` | 生成したドメインモデルがリポジトリに保存されること |
| バリデーション異常系 | `バリデーション` | ドメインモデルのinitブロック違反で例外が発生すること |
| enum変換異常系 | `バリデーション` | 不正なenum文字列で例外が発生すること |

## フェイクRepositoryの作り方

Repositoryインターフェースの匿名実装を使い、保存されたオブジェクトをリストで保持する。

```kotlin
private val savedShops = mutableListOf<Shop>()

private val fakeRepository = object : ShopRepository {
    override fun save(shop: Shop) {
        savedShops.add(shop)
    }
}

private val usecase = CreateShopUsecase(fakeRepository)
```

## UUID自動生成の検証

```kotlin
@Test
fun `ShopIdがUUID形式で自動生成される`() {
    val shop = usecase.execute(createRequest())
    val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    assertTrue(uuidRegex.matches(shop.id.value))
}
```

## 標準実装リファレンス

以下はユースケーステストを実装する際のテンプレートとなるコード例。

```kotlin
package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.repository.ShopRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CreateShopUsecaseTest {

    private val savedShops = mutableListOf<Shop>()

    private val fakeRepository = object : ShopRepository {
        override fun save(shop: Shop) {
            savedShops.add(shop)
        }
    }

    private val usecase = CreateShopUsecase(fakeRepository)

    private fun createRequest(
        shopifyShopId: String = "test-shop-001",
        name: String = "テスト店舗",
        introduction: String? = "テスト紹介文",
        particular: String? = "テストこだわり",
        images: List<CreateShopRequest.ImageRequest> = listOf(
            CreateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/image.png"),
        ),
    ): CreateShopRequest = CreateShopRequest(
        shopifyShopId = shopifyShopId,
        name = name,
        introduction = introduction,
        particular = particular,
        images = images,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にShopを作成できる`() {
            val request = createRequest()
            val shop = usecase.execute(request)

            assertEquals("test-shop-001", shop.shopifyShopId.value)
            assertEquals("テスト店舗", shop.name)
            assertEquals("テスト紹介文", shop.introduction)
            assertEquals("テストこだわり", shop.particular)
            assertEquals(1, shop.images.size)
            assertEquals(ShopImageType.MAIN, shop.images[0].type)
            assertEquals("https://example.com/image.png", shop.images[0].imageUrl.value)
        }
    }

    @Nested
    inner class UUID自動生成 {
        @Test
        fun `ShopIdがUUID形式で自動生成される`() {
            val shop = usecase.execute(createRequest())
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(shop.id.value))
        }

        @Test
        fun `ShopImageIdがUUID形式で自動生成される`() {
            val shop = usecase.execute(createRequest())
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(shop.images[0].id.value))
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `introductionがnullでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(introduction = null))
            assertNull(shop.introduction)
        }

        @Test
        fun `particularがnullでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(particular = null))
            assertNull(shop.particular)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像なしでもShopを作成できる`() {
            val shop = usecase.execute(createRequest(images = emptyList()))
            assertEquals(0, shop.images.size)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `作成したShopがリポジトリに保存される`() {
            savedShops.clear()
            usecase.execute(createRequest())
            assertEquals(1, savedShops.size)
            assertEquals("テスト店舗", savedShops[0].name)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(name = ""))
            }
        }

        @Test
        fun `不正な画像種別の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(images = listOf(
                    CreateShopRequest.ImageRequest(type = "INVALID", imageUrl = "https://example.com/image.png"),
                )))
            }
        }
    }
}
```

## チェックリスト

新しいユースケーステストを実装するときに確認すること:

- [ ] テストがカテゴリごとに @Nested inner class でグルーピングされているか
- [ ] フェイクRepositoryを匿名実装で作成しているか
- [ ] リクエスト生成用のファクトリメソッド `createRequest(...)` を用意しているか
- [ ] 正常系で全フィールドを検証しているか
- [ ] サーバー生成IDごとにUUID形式の自動生成テストがあるか
- [ ] nullable項目ごとにnullテストがあるか
- [ ] 子エンティティの空リストテストがあるか
- [ ] リポジトリへの保存を検証しているか
- [ ] ドメインバリデーション違反（`IllegalArgumentException`）のテストがあるか
- [ ] 不正なenum文字列（`IllegalArgumentException`）のテストがあるか
