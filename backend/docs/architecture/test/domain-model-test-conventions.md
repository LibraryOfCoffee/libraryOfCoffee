# ドメインモデルテスト規約

共通ルールは [テスト共通規約](test-conventions.md) を参照。

## ファイル構成

```
common/src/test/kotlin/com/mametosho/domain/model/
├── {aggregate}/
│   ├── {Aggregate}Test.kt           # 集約ルートのテスト
│   ├── {Entity}Test.kt              # エンティティのテスト（ロジックがある場合）
│   └── {ValueObject}Test.kt         # 値オブジェクトのテスト（バリデーションがある場合）
```

## 命名パターン

| パターン | 用途 | 例 |
|---------|------|-----|
| `正常に{X}を生成できる` | 正常系の生成テスト | `正常にShopを生成できる` |
| `{field}がnullでも生成できる` | nullable項目のテスト | `introductionがnullでも生成できる` |
| `{field}が{condition}の場合は例外が発生する` | バリデーションの異常系 | `priceが負の値の場合は例外が発生する` |
| `{field}が{boundary}の場合は生成できる` | 境界値の正常系 | `priceが0の場合は生成できる` |
| `{operation}できる` | ドメインメソッドの正常系 | `珈琲豆を自己選択できる` |
| `{condition}の場合は{operation}すると例外が発生する` | ドメインメソッドの異常系 | `発送済みの場合は珈琲豆を選択すると例外が発生する` |

## テストカテゴリと書くべきテスト

テストはカテゴリごとに `@Nested inner class` でグルーピングする。

### 集約ルート・エンティティ

| カテゴリ | ネストクラス名 | 書くべきテスト |
|---------|--------------|-------------|
| 生成テスト | `生成テスト` | 正常系の生成 |
| nullable項目 | `生成テスト` または専用クラス | nullable各項目がnullでも生成できること |
| バリデーション | `バリデーション` | `init` ブロックの `require` / `check` ごとに正常系・異常系 |
| コレクションの一意性 | `バリデーション` | 重複不可の制約がある場合、正常系・異常系 |
| ドメインメソッド | `{メソッド名}` (e.g. `addChild`, `cancel`) | 各メソッドの正常系・状態制約による異常系 |

### 値オブジェクト

| カテゴリ | ネストクラス名 | 書くべきテスト |
|---------|--------------|-------------|
| 境界値テスト | `生成テスト`, `バリデーション` | バリデーションの境界値（0、最小値、負の値など） |
| 日付範囲 | `バリデーション` | from/toの前後関係、同一日 |

## 標準実装リファレンス

以下は集約ルートのテストを実装する際のテンプレートとなるコード例。

```kotlin
package com.mametosho.domain.model.plan

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanTest {

    private fun createPlan(
        price: Int = 3800,
        beanQuantity: Int = 4,
    ): Plan = Plan(
        id = PlanId("00000000-0000-4000-8000-00000000000d"),
        shopifyPlanId = ShopifyPlanId("shopify-plan-1"),
        label = "定番",
        gramWeight = 60,
        beanQuantity = beanQuantity,
        price = price,
        type = PlanType.SUBSCRIPTION,
        isRecommended = true,
    )

    @Nested
    inner class 生成テスト {
        @Test
        fun `正常にPlanを生成できる`() {
            val plan = createPlan()
            assertEquals(3800, plan.price)
            assertEquals(4, plan.beanQuantity)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `priceが0の場合は生成できる`() {
            val plan = createPlan(price = 0)
            assertEquals(0, plan.price)
        }

        @Test
        fun `priceが負の値の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createPlan(price = -1)
            }
        }

        @Test
        fun `beanQuantityが1の場合は生成できる`() {
            val plan = createPlan(beanQuantity = 1)
            assertEquals(1, plan.beanQuantity)
        }

        @Test
        fun `beanQuantityが0の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                createPlan(beanQuantity = 0)
            }
        }
    }
}
```

## チェックリスト

新しいドメインモデルのテストを実装するときに確認すること:

- [ ] テストファイルが本体と同じパッケージに配置されているか
- [ ] テストメソッド名が日本語で記述されているか
- [ ] テストがカテゴリごとに @Nested inner class でグルーピングされているか
- [ ] ファクトリメソッドを使ってテストデータを生成しているか
- [ ] IDがUUIDv4形式で統一されているか
- [ ] `init` ブロックのバリデーションごとに正常系・異常系のテストがあるか
- [ ] nullable項目のテストがあるか
- [ ] ドメインメソッドの正常系と状態制約のテストがあるか
- [ ] `require` 違反は `IllegalArgumentException`、`check` 違反は `IllegalStateException` で検証しているか
