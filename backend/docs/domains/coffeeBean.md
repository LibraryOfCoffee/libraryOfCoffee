# CoffeeBean 集約

## 概要

店舗が提供する珈琲豆を表す集約。
Shopifyの商品と1対1で紐づく。産地、焙煎度、精製方法、テイスト評価などの情報を持つ。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する。初期状態は任意に選択でき、デフォルトは下書き（DRAFT）
- **更新**: 珈琲豆情報・画像・テイスト評価の変更
- **公開状態の切り替え**: 管理者が DRAFT ⇄ PUBLISHED を双方向に切り替えられる。DRAFT の珈琲豆はCS（一般ユーザー向け）には公開されない
- **自動無効化**: 提供元の店舗が参画落ち（DROPPED）になると、その店舗に属する全珈琲豆が INVALIDATED に自動設定される。INVALIDATED になると以降の更新は不可

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
| roastLevel | RoastLevel | 焙煎度（light / cinnamon / medium / city / french） |
| processingMethod | ProcessingMethod | 精製方法（fully_washed / washed / anaerobic_washed / thermal_shock_natural / natural / anaerobic_natural / dry_on_tree_natural / lactic_natural / wet_hulling / honey / lado_a_lado_process / lado_a_lado_process_fully_washed（ブレンドコーヒー向けの一時的な複合種別）） |
| isSpecialty | Boolean | スペシャルティコーヒーかどうか |
| publishStatus | PublishStatus | 公開状態（DRAFT / PUBLISHED / INVALIDATED） |
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
- INVALIDATED 状態の珈琲豆は更新できない

## PublishStatus（Enum）

公開状態を表すEnum。INVALIDATED は店舗が参画落ち（DROPPED）になった際にシステムが自動設定する終端状態。
PUBLISHED 以外の珈琲豆はCS（一般ユーザー向けAPI）には公開されない。

| 値 | 意味 | 設定者 |
|---|---|---|
| `DRAFT` | 下書き・非公開 | 管理者 |
| `PUBLISHED` | 公開 | 管理者 |
| `INVALIDATED` | 無効化（終端・変更不可） | システム自動設定（店舗DROPPED時） |

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
