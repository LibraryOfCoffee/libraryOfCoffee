# ドメインモデル実装規約

## ファイル構成

1クラス/1enum/1value class = 1ファイルを原則とする。

```
domain/model/
├── {aggregate}/              # 集約ごとにパッケージを分ける
│   ├── {Aggregate}.kt        # 集約ルート（data class）
│   ├── {AggregateId}.kt      # 集約ルートのID（value class）
│   ├── {Entity}.kt           # エンティティ（data class）
│   ├── {EntityId}.kt         # エンティティのID（value class）
│   ├── {Enum}.kt             # Enum（1ファイル1enum）
│   └── {ValueObject}.kt      # 値オブジェクト（value class / data class）
├── shared/                   # 複数集約で共用する値オブジェクト
│   └── ImageUrl.kt
└── Sample.kt                 # （既存サンプル）
```

### 他集約のIDを参照する場合

参照先のIDはその集約のパッケージに定義する。

```kotlin
// domain/model/shop/ShopId.kt — Shop集約が所有
package com.mametosho.domain.model.shop
@JvmInline value class ShopId(val value: String)

// domain/model/coffeebean/CoffeeBean.kt — ShopIdをimportして使う
import com.mametosho.domain.model.shop.ShopId
```

## ID型

`@JvmInline value class` を使う。ランタイムコストなしで型安全性を確保する。

IDには **ULID** を採用する。`value: String` で保持する。

```kotlin
@JvmInline
value class CoffeeBeanId(val value: String)
```

- ドメインモデルのID → `value: String`（ULID）
- 外部システムのID（Shopify等）→ `value: String`

## Enum

1ファイルに1enumを定義する。値は `UPPER_SNAKE_CASE` で記述する。

```kotlin
// RoastLevel.kt
enum class RoastLevel {
    LIGHT,
    MEDIUM,
    CITY,
    FRENCH,
}
```

## 集約ルート

```kotlin
data class CoffeeBean(
    val id: CoffeeBeanId,
    // ...
    val tastes: List<CoffeeBeanTaste>,
) {
    init {
        // 不変条件のバリデーション
    }
}
```

- Spring依存を持たせない
- 不変条件は `init` ブロックで `require` を使って検証する

## エンティティ

集約ルートと同じパッケージに配置する。

```kotlin
data class CoffeeBeanTaste(
    val id: CoffeeBeanTasteId,
    val tasteId: TasteId,
    val evaluationValue: Int,
) {
    init {
        require(evaluationValue >= 0) { "evaluationValue must be non-negative" }
    }
}
```

## 例外メッセージ

`require` / `check` / 例外のメッセージは **英語** で記述する。

```kotlin
// Good
require(evaluationValue >= 0) { "evaluationValue must be non-negative" }

// Bad
require(evaluationValue >= 0) { "evaluationValueは0以上でなければなりません" }
```
