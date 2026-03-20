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

IDには **UUIDv4** を採用する。`value: String` で保持する。

すべてのID型は `init` ブロックでバリデーションを行う。

```kotlin
@JvmInline
value class CoffeeBeanId(val value: String) {
    init {
        require(value.isNotBlank()) { "CoffeeBeanId must not be blank" }
        require(UUID_REGEX.matches(value)) { "CoffeeBeanId must be a valid UUID format" }
    }

    companion object {
        private val UUID_REGEX = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", RegexOption.IGNORE_CASE)
    }
}
```

- ドメインモデルのID → `value: String`（UUIDv4）、UUID形式バリデーション付き
- 外部システムのID（Shopify等）→ `value: String`、空白不可 + 255文字以内のバリデーション付き

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

## ファクトリメソッド（`create`）

集約ルートの新規生成ロジック（ID自動生成、子エンティティの構築）は、集約ルートの `companion object` に `create()` メソッドとして定義する。Usecase側ではプリミティブ値を渡して `create()` を呼ぶだけにする。

```kotlin
data class Shop(
    val id: ShopId,
    val shopifyShopId: ShopifyShopId,
    val name: String,
    val introduction: String?,
    val particular: String?,
    val images: List<ShopImage>,
) {
    init { /* バリデーション */ }

    companion object {
        fun create(
            shopifyShopId: String,
            name: String,
            introduction: String?,
            particular: String?,
            images: List<Pair<String, String>>,  // type to imageUrl
        ): Shop = Shop(
            id = ShopId(UUID.randomUUID().toString()),
            shopifyShopId = ShopifyShopId(shopifyShopId),
            name = name,
            introduction = introduction,
            particular = particular,
            images = images.map { (type, imageUrl) ->
                ShopImage(
                    id = ShopImageId(UUID.randomUUID().toString()),
                    type = ShopImageType.valueOf(type),
                    imageUrl = ImageUrl(imageUrl),
                )
            },
        )
    }
}
```

### Usecaseでの使い方

```kotlin
@Service
class CreateShopUsecase(private val shopRepository: ShopRepository) {
    @Transactional
    fun execute(request: CreateShopRequest): Shop {
        val shop = Shop.create(
            shopifyShopId = request.shopifyShopId,
            name = request.name,
            introduction = request.introduction,
            particular = request.particular,
            images = request.images.map { it.type to it.imageUrl },
        )
        shopRepository.save(shop)
        return shop
    }
}
```

### ルール

- ID生成（`UUID.randomUUID()`）は `create()` 内で行う。Usecaseで行わない
- 子エンティティの構築も `create()` 内で行う
- `create()` の引数はプリミティブ型（`String`, `Int`, `Boolean`, `Pair` 等）で受け取り、値オブジェクトへの変換は `create()` 内で行う
- DBからの復元にはコンストラクタを直接使う（`create()` は新規生成専用）

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

## KDoc

すべてのドメインモデルクラスにKDocを付与する。KDocは **日本語** で記述する。

KDoc内のドメイン用語は `docs/domains/` 配下の仕様書で定義された **ユビキタス言語** に従う。
Kotlinの定数名（`ACTIVE`, `CANCELED` 等）ではなく、仕様書の表記（`active`, `canceled` 等）を使用する。

```kotlin
// Good — 仕様書の用語に従う
/** 契約ステータス。active → canceled の遷移。 */

// Bad — Kotlin定数名を使ってしまっている
/** 契約ステータス。ACTIVE → CANCELED の遷移。 */
```

### 集約ルート・エンティティ・値オブジェクト

クラスの概要、ドメイン上の役割、`@property` タグでプロパティの説明を記載する。

```kotlin
/**
 * 顧客を表す集約ルート。
 *
 * Shopifyの顧客と1対1で紐づき、顧客が持つサブスクリプション契約のライフサイクルを管理する。
 *
 * @property id 顧客ID
 * @property shopifyCustomerId Shopifyの顧客ID。システム内で一意
 * @property status 顧客ステータス
 * @property subscriptions サブスクリプション契約一覧
 */
data class Customer(...)
```

### メソッド

メソッドの概要、`@param`、`@return`、`@throws` を記載する。

```kotlin
/**
 * 契約を追加する。
 *
 * @param id 新しい契約のID
 * @param subscriptionPlanId 契約するプランのID
 * @param contractedFrom 契約開始日
 * @return 契約が追加された新しい[Customer]
 * @throws IllegalStateException 退会済み（withdrawn）の顧客の場合
 */
fun addSubscription(...): Customer
```

### ID型（value class）

1行のKDocで役割を簡潔に記述する。

```kotlin
/** 顧客のID。 */
@JvmInline
value class CustomerId(val value: String)
```

### Enum

クラスの概要と状態遷移がある場合はその説明を記載する。

```kotlin
/**
 * 顧客ステータス。
 *
 * active → withdrawn の不可逆な遷移のみ許可される。
 */
enum class CustomerStatus { ... }
```

## 例外メッセージ

`require` / `check` / 例外のメッセージは **英語** で記述する。

```kotlin
// Good
require(evaluationValue >= 0) { "evaluationValue must be non-negative" }

// Bad
require(evaluationValue >= 0) { "evaluationValueは0以上でなければなりません" }
```
