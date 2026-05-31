# Plan 集約

## 概要

購入プランを表す集約。定期便（SUBSCRIPTION）と単品購入（SINGLE）を並列な関係で管理する。
プラン表示名・グラム数・豆の種類数・価格・種別の組み合わせで1レコードを構成する。

## ライフサイクル

- **登録**: DB初期データまたは管理者が登録する
- **更新**: 価格等の変更

## Plan

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | PlanId | プランID |
| shopifyPlanId | ShopifyPlanId | ShopifyのプランID。システム内で一意 |
| label | String | プラン表示名（例: はじめて、定番、たっぷり） |
| gramWeight | Int | 1種あたりのグラム数（30 / 60 / 90） |
| beanQuantity | Int | 豆の種類数（3 / 4 / 5） |
| price | Int | 価格 |
| type | PlanType | プラン種別（SUBSCRIPTION / SINGLE） |
| isRecommended | Boolean | おすすめバッジを表示するか |

### 不変条件

- ShopifyPlanId はシステム内で一意でなければならない
- price は 0 以上でなければならない
- beanQuantity は 1 以上でなければならない
- gramWeight は 30 / 60 / 90 のいずれかでなければならない

## PlanType

| 値 | 説明 |
|----|------|
| SUBSCRIPTION | 定期便プラン（毎月お届け） |
| SINGLE | 単品購入プラン（1回のみ） |

## 関連する集約

| 集約 | 関連 | 説明 |
|------|------|------|
| [Customer](./customer.md) | 被参照 | CustomerSubscription がこのプランを参照する |
