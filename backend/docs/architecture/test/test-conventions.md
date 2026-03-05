# テスト共通規約

全層で共通するテストの実装ルール。層固有の規約は各テスト規約ドキュメントを参照すること。

| 層 | ドキュメント |
|---|-------------|
| ドメインモデル | [`domain-model-test-conventions.md`](domain-model-test-conventions.md) |

## テストフレームワーク

- **JUnit 5**（`org.junit.jupiter`）
- **kotlin-test**（`kotlin.test`）

```kotlin
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
```

## ファイル配置

テストファイルは本体と同じパッケージ構成に配置する。

## テストメソッドの命名

バッククォート付きの **日本語** でテスト名を記述する。

```kotlin
@Test
fun `正常にCoffeeBeanを生成できる`() { ... }

@Test
fun `nameが空白の場合は例外が発生する`() { ... }
```

## ファクトリメソッド

テストデータの生成には `private fun create{Entity}(...)` ファクトリメソッドを使う。テスト対象のパラメータのみを引数に公開し、それ以外はデフォルト値を設定する。

```kotlin
private fun createSubscriptionPlan(
    price: Int = 3000,
    beanQuantity: Int = 3,
): SubscriptionPlan = SubscriptionPlan(
    id = SubscriptionPlanId("00000000-0000-4000-8000-000000000001"),
    shopifySubscriptionId = ShopifySubscriptionId("shopify-sub-1"),
    price = price,
    beanQuantity = beanQuantity,
)
```

### IDの書き方

テスト用のIDはUUIDv4形式に揃える。ゼロ埋めプレフィックスと末尾の連番で可読性を確保する。

```kotlin
// Good
CoffeeBeanId("00000000-0000-4000-8000-000000000001")
CustomerSubscriptionId("00000000-0000-4000-8000-000000000002")

// Bad — 任意の文字列
CoffeeBeanId("test-bean-id")
```

### nullable項目のテスト

ファクトリメソッドで非nullのデフォルト値を設定し、`copy()` でnullに変更してテストする。

```kotlin
@Test
fun `farmがnullでも生成できる`() {
    val bean = createCoffeeBean().copy(farm = null)
    assertNull(bean.farm)
}
```

## アサーション

| 用途 | メソッド |
|------|---------|
| 値の一致 | `assertEquals(expected, actual)` |
| null確認 | `assertNull(value)` |
| 真偽確認 | `assertTrue(condition)` |
| 例外確認（`require` 違反） | `assertThrows<IllegalArgumentException> { ... }` |
| 例外確認（`check` 違反） | `assertThrows<IllegalStateException> { ... }` |
