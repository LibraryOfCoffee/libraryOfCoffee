# CoffeeBean 集約

## 概要

店舗が提供する珈琲豆を表す集約。
Shopifyの商品と1対1で紐づく。産地、焙煎度、精製方法、テイスト評価などの情報を持つ。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する
- **更新**: 珈琲豆情報・画像・テイスト評価の変更

## CoffeeBean

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | CoffeeBeanId | 珈琲豆ID |
| shopId | ShopId | 提供元店舗のID |
| shopifyBeanId | ShopifyBeanId | Shopifyの商品ID。システム内で一意 |
| name | String | 珈琲豆名 |
| description | String | 説明 |
| origin | String | 産地 |
| farm | String? | 農園 |
| roastLevel | RoastLevel | 焙煎度（light / medium / city / french） |
| processingMethod | ProcessingMethod | 精製方法（fully_washed / washed / thermal_shock_natural / natural / wet_hulling / honey） |
| isSpecialty | Boolean | スペシャルティコーヒーかどうか |
| images | List\<CoffeeBeanImage\> | 珈琲豆画像一覧 |
| tastes | List\<CoffeeBeanTaste\> | テイスト評価一覧 |

### 不変条件

- ShopifyBeanIdはシステム内で一意でなければならない
- nameは必須
- descriptionは必須
- originは必須
- roastLevelは必須
- processingMethodは必須
- shopIdは存在するShopを参照しなければならない

## CoffeeBeanImage（エンティティ）

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | CoffeeBeanImageId | 画像ID |
| type | CoffeeBeanImageType | 画像の種別（main など） |
| imageUrl | ImageUrl | 画像URL |

## CoffeeBeanTaste（エンティティ）

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | CoffeeBeanTasteId | テイスト評価ID |
| tasteId | TasteId | テイストのID |
| evaluationValue | Int | 評価値 |

### 不変条件

- 同一珈琲豆に同じTasteの評価を複数持つことはできない
- evaluationValueは0以上でなければならない

## 関連する集約

| 集約 | 関連 | 説明 |
|------|------|------|
| [Shop](./shop.md) | ID参照 | 珈琲豆を提供する店舗 |
| [Taste](./taste.md) | ID参照 | テイスト評価の種別 |
| [MonthlySubscriptionDetail](./monthlySubscriptionDetail.md) | 被参照 | 月次詳細の選択・発送豆として参照される |
| [CoffeeListGroup](./coffeeListGroup.md) | 被参照 | 珈琲リストの子要素として参照される |
