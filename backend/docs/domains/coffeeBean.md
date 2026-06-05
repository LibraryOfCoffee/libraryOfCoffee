# CoffeeBean 集約

## 概要

店舗が提供する珈琲豆を表す集約。
Shopifyの商品と1対1で紐づく。産地、焙煎度、精製方法、テイスト評価などの情報を持つ。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する。初期状態は任意に選択でき、デフォルトは下書き（draft）
- **更新**: 珈琲豆情報・画像・テイスト評価の変更
- **公開状態の切り替え**: 管理者が draft ⇄ published を双方向に切り替えられる。draft の珈琲豆はCS（一般ユーザー向け）には公開されない

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
| publishStatus | PublishStatus | 公開状態（draft / published） |
| images | List\<CoffeeBeanImage\> | 珈琲豆画像一覧 |
| tastes | List\<CoffeeBeanTaste\> | テイスト評価一覧 |

### 不変条件

- ShopifyBeanIdはシステム内で一意でなければならない
- nameは必須（255文字以内）
- descriptionは必須（10000文字以内）
- originは必須（255文字以内）
- farmは非null時、空白不可（255文字以内）
- roastLevelは必須
- processingMethodは必須
- shopIdは存在するShopを参照しなければならない
- **images は必須**。少なくとも1枚の画像を持たなければならない
- **MAINタイプの画像はちょうど1枚**でなければならない（重複不可）
- **tastes は必須**。全テイスト種別（酸味・苦味・甘味・コク・香り）の評価値を持たなければならない

## PublishStatus（Enum）

公開状態を表すEnum。draft（下書き・非公開）と published（公開）の双方向の遷移が可能。
draft の珈琲豆はCS（一般ユーザー向けAPI）には公開されない。Shop集約と共有する値オブジェクト。

`DRAFT`, `PUBLISHED`

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
- evaluationValueは0以上5以下でなければならない

## 関連する集約

| 集約 | 関連 | 説明 |
|------|------|------|
| [Shop](./shop.md) | ID参照 | 珈琲豆を提供する店舗 |
| [Taste](./taste.md) | ID参照 | テイスト評価の種別 |
| [MonthlySubscriptionDetail](./monthlySubscriptionDetail.md) | 被参照 | 月次詳細の選択・発送豆として参照される |
