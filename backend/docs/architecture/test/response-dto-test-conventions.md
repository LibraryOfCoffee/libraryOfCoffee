# レスポンスDTOテスト規約

共通ルールは [テスト共通規約](test-conventions.md) を参照。

## 概要

レスポンスDTO（`*Response`）のテストは、ドメインモデルからDTOへの `from()` 変換を検証する単体テストとして実装する。

## ファイル構成

```
admin-api/src/test/kotlin/com/mametosho/admin/presentation/dto/response/
└── {Aggregate}ResponseTest.kt
```

## テストカテゴリと書くべきテスト

テストはカテゴリごとに `@Nested inner class` でグルーピングする。

| カテゴリ | ネストクラス名 | 書くべきテスト |
|---------|--------------|-------------|
| 正常系変換 | `正常系変換` | ドメインモデルからレスポンスDTOに正しく変換されること（全フィールド検証） |
| nullable項目 | `nullable項目` | nullable項目がnullの場合もレスポンスに変換できること |
| 空コレクション | `空コレクション` | 子エンティティのリストが空の場合もレスポンスに変換できること |

## 標準実装リファレンス

以下はレスポンスDTOテストを実装する際のテンプレートとなるコード例。

```kotlin
package com.mametosho.admin.presentation.dto.response

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShopResponseTest {

    private fun createShop(
        introduction: String? = "テスト紹介文",
        particular: String? = "テストこだわり",
        images: List<ShopImage> = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/image.png"),
            ),
        ),
    ): Shop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000001"),
        shopifyShopId = ShopifyShopId("test-shop-001"),
        name = "テスト店舗",
        introduction = introduction,
        particular = particular,
        images = images,
    )

    @Nested
    inner class 正常系変換 {
        @Test
        fun `正常にShopからShopResponseに変換できる`() {
            val response = ShopResponse.from(createShop())

            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
            assertEquals("test-shop-001", response.shopifyShopId)
            assertEquals("テスト店舗", response.name)
            assertEquals("テスト紹介文", response.introduction)
            assertEquals("テストこだわり", response.particular)
            assertEquals(1, response.images.size)
            assertEquals("00000000-0000-4000-8000-000000000011", response.images[0].id)
            assertEquals("MAIN", response.images[0].type)
            assertEquals("https://example.com/image.png", response.images[0].imageUrl)
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `introductionがnullの場合もレスポンスに変換できる`() {
            val response = ShopResponse.from(createShop(introduction = null))
            assertNull(response.introduction)
        }

        @Test
        fun `particularがnullの場合もレスポンスに変換できる`() {
            val response = ShopResponse.from(createShop(particular = null))
            assertNull(response.particular)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像が空の場合もレスポンスに変換できる`() {
            val response = ShopResponse.from(createShop(images = emptyList()))
            assertEquals(0, response.images.size)
        }
    }
}
```

## チェックリスト

新しいレスポンスDTOテストを実装するときに確認すること:

- [ ] テストがカテゴリごとに @Nested inner class でグルーピングされているか
- [ ] ドメインモデル生成用のファクトリメソッド `create{Aggregate}(...)` を用意しているか
- [ ] ファクトリメソッドの引数にnullable項目・コレクションを公開しているか
- [ ] 正常系で `from()` の全フィールドを検証しているか（ID値、enum名、ネストした子エンティティ含む）
- [ ] nullable項目ごとにnull変換テストがあるか
- [ ] 子エンティティのリストが空の場合の変換テストがあるか
