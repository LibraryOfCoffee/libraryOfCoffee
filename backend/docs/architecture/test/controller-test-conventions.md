# コントローラテスト規約

共通ルールは [テスト共通規約](test-conventions.md) を参照。

## 概要

コントローラ（`*Controller`）のテストは、ユースケースをフェイク実装に差し替えた単体テストとして実装する。HTTPステータスコードとレスポンスボディの内容を検証する。

## ファイル構成

```
admin-api/src/test/kotlin/com/mametosho/admin/presentation/controller/
└── {Aggregate}ControllerTest.kt
```

## テストカテゴリと書くべきテスト

テストはカテゴリごとに `@Nested inner class` でグルーピングする。

| カテゴリ | ネストクラス名 | 書くべきテスト |
|---------|--------------|-------------|
| ステータスコード | `正常系` | 正常系で期待するHTTPステータス（201など）が返ること |
| レスポンスボディ | `レスポンスボディ` | レスポンスボディの全フィールドが正しいこと |
| 子エンティティ | `レスポンスボディ` | ネストした子エンティティ（images, tastesなど）がレスポンスに含まれること |

## フェイクUsecaseの作り方

ユースケースクラスを継承した匿名クラスで `execute` をオーバーライドし、固定のドメインモデルを返す。コンストラクタにはフェイクRepositoryを渡す。

```kotlin
private fun createController(shop: Shop = sampleShop): ShopController {
    val fakeUsecase = object : CreateShopUsecase(
        object : com.mametosho.domain.repository.ShopRepository {
            override fun save(shop: Shop) = Unit
        },
    ) {
        override fun execute(request: CreateShopRequest): Shop = shop
    }
    return ShopController(fakeUsecase)
}
```

## 標準実装リファレンス

以下はコントローラテストを実装する際のテンプレートとなるコード例。

```kotlin
package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.CreateShopUsecase
import com.mametosho.admin.presentation.dto.request.CreateShopRequest
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.shop.ShopImage
import com.mametosho.domain.model.shop.ShopImageId
import com.mametosho.domain.model.shop.ShopImageType
import com.mametosho.domain.model.shop.ShopifyShopId
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopControllerTest {

    private val sampleShop = Shop(
        id = ShopId("00000000-0000-4000-8000-000000000001"),
        shopifyShopId = ShopifyShopId("test-shop-001"),
        name = "テスト店舗",
        introduction = "テスト紹介文",
        particular = "テストこだわり",
        images = listOf(
            ShopImage(
                id = ShopImageId("00000000-0000-4000-8000-000000000011"),
                type = ShopImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/image.png"),
            ),
        ),
    )

    private fun createController(shop: Shop = sampleShop): ShopController {
        val fakeUsecase = object : CreateShopUsecase(
            object : com.mametosho.domain.repository.ShopRepository {
                override fun save(shop: Shop) = Unit
            },
        ) {
            override fun execute(request: CreateShopRequest): Shop = shop
        }
        return ShopController(fakeUsecase)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `正常に店舗を登録すると201が返る`() {
            val controller = createController()
            val request = CreateShopRequest(
                shopifyShopId = "test-shop-001",
                name = "テスト店舗",
                introduction = "テスト紹介文",
                particular = "テストこだわり",
                images = listOf(
                    CreateShopRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/image.png"),
                ),
            )

            val response = controller.createShop(request)

            assertEquals(HttpStatus.CREATED, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
            assertEquals("test-shop-001", response.body?.shopifyShopId)
            assertEquals("テスト店舗", response.body?.name)
            assertEquals("テスト紹介文", response.body?.introduction)
            assertEquals("テストこだわり", response.body?.particular)
            assertEquals(1, response.body?.images?.size)
            assertEquals("MAIN", response.body?.images?.get(0)?.type)
        }
    }

    @Nested
    inner class レスポンスボディ {
        @Test
        fun `レスポンスに画像情報が含まれる`() {
            val controller = createController()
            val request = CreateShopRequest(
                shopifyShopId = "test-shop-001",
                name = "テスト店舗",
                introduction = null,
                particular = null,
                images = emptyList(),
            )

            val response = controller.createShop(request)

            assertEquals("00000000-0000-4000-8000-000000000011", response.body?.images?.get(0)?.id)
            assertEquals("https://example.com/image.png", response.body?.images?.get(0)?.imageUrl)
        }
    }
}
```

## チェックリスト

新しいコントローラテストを実装するときに確認すること:

- [ ] テストがカテゴリごとに @Nested inner class でグルーピングされているか
- [ ] 固定のドメインモデル（`sampleXxx`）をテストクラスのプロパティとして定義しているか
- [ ] フェイクUsecaseを匿名クラスで作成し `execute` をオーバーライドしているか
- [ ] コントローラ生成用のファクトリメソッド `createController(...)` を用意しているか
- [ ] HTTPステータスコードを検証しているか
- [ ] レスポンスボディの主要フィールドを検証しているか
- [ ] ネストした子エンティティ（images, tastesなど）の検証テストがあるか
