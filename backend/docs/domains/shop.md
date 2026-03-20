# Shop 集約

## 概要

珈琲豆を提供する店舗を表す集約。
Shopifyのショップと1対1で紐づく。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する
- **更新**: 店舗情報・画像の変更

## Shop

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | ShopId | 店舗ID |
| shopifyShopId | ShopifyShopId | ShopifyのショップID。システム内で一意 |
| name | String | 店舗名 |
| introduction | String? | 店舗紹介 |
| particular | String? | こだわり |
| images | List\<ShopImage\> | 店舗画像一覧 |

### 不変条件

- ShopifyShopIdはシステム内で一意でなければならない
- nameは必須（255文字以内）
- introductionは非null時、空白不可（10000文字以内）
- particularは非null時、空白不可（10000文字以内）

## ShopImage（エンティティ）

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | ShopImageId | 画像ID |
| type | ShopImageType | 画像の種別（main など） |
| imageUrl | ImageUrl | 画像URL |

## 関連する集約

| 集約 | 関連 | 説明 |
|------|------|------|
| [CoffeeBean](./coffeeBean.md) | 被参照 | 珈琲豆がこの店舗を参照する |
