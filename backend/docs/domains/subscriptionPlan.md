# SubscriptionPlan 集約

## 概要

サブスクリプションのプランを表す集約。
Shopifyのサブスクリプションと1対1で紐づく。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する
- **更新**: 価格等の変更

## SubscriptionPlan

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | SubscriptionPlanId | プランID |
| shopifySubscriptionId | ShopifySubscriptionId | ShopifyのサブスクリプションID。システム内で一意 |
| price | Int | 価格 |
| beanQuantity | Int | 1回の配送で届く珈琲豆の数 |

### 不変条件

- ShopifySubscriptionIdはシステム内で一意でなければならない
- priceは0以上でなければならない
- beanQuantityは1以上でなければならない

## 関連する集約

| 集約 | 関連 | 説明 |
|------|------|------|
| [Customer](./customer.md) | 被参照 | CustomerSubscriptionがこのプランを参照する |
