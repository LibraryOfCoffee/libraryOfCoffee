# CoffeeListGroup 集約

## 概要

CSサイトに表示する珈琲豆のリストを表す集約。
珈琲豆をグルーピングして顧客に提示するために使用する。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する
- **更新**: リストの説明変更、珈琲豆の追加・削除

## CoffeeListGroup

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | CoffeeListGroupId | 珈琲リストID |
| description | String? | リストの説明 |
| children | List\<CoffeeListChild\> | リストに含まれる珈琲豆 |

### メソッド

| メソッド | 説明 |
|---------|------|
| addChild(coffeeBeanId) | 珈琲豆をリストに追加する |
| removeChild(coffeeListChildId) | 珈琲豆をリストから削除する |

### 不変条件

- 同一リストに同じ珈琲豆を重複して追加できない
- 全てのリストを通じて、同じ珈琲豆は1つのリストにしか所属できない

## CoffeeListChild（エンティティ）

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | CoffeeListChildId | 子要素ID |
| coffeeBeanId | CoffeeBeanId | 珈琲豆のID |

## 関連する集約

| 集約 | 関連 | 説明 |
|------|------|------|
| [CoffeeBean](./coffeeBean.md) | ID参照 | リストに含まれる珈琲豆 |
