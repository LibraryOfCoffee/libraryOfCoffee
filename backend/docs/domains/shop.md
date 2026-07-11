# Shop 集約

## 概要

珈琲豆を提供する店舗を表す集約。
Shopifyのショップと1対1で紐づく。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する。初期状態は参画前（BEFORE_PARTICIPATION）または参画中（PARTICIPATING）を選択できる
- **更新**: 店舗情報・画像の変更
- **参画ステータスの遷移**: 管理者が BEFORE_PARTICIPATION → PARTICIPATING → DROPPED の直線遷移のみ可能。DROPPED になると以降の変更は不可。PARTICIPATING の店舗のみ CS（一般ユーザー向け）に公開される

## Shop

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | ShopId | 店舗ID |
| shopifyShopId | ShopifyShopId | ShopifyのショップID。システム内で一意 |
| name | String | 店舗名 |
| introduction | String? | 店舗紹介 |
| particular | String? | こだわり |
| shopUrl | String | 店舗URL |
| prefecture | Prefecture | 都道府県 |
| participationStatus | ParticipationStatus | 参画ステータス |
| images | List\<ShopImage\> | 店舗画像一覧 |

### 不変条件

- ShopifyShopIdはシステム内で一意でなければならない
- nameは必須（255文字以内）
- introductionは非null時、空白不可（10000文字以内）
- particularは非null時、空白不可（10000文字以内）
- shopUrlは必須、空白不可（2048文字以内）
- prefectureは必須
- LOGO画像はちょうど1枚（必須）

## ParticipationStatus（Enum）

参画ステータスを表すEnum。BEFORE_PARTICIPATION → PARTICIPATING → DROPPED の直線遷移のみ許可。
DROPPED の店舗はステータス変更不可（終端状態）。PARTICIPATING の店舗のみ CS（一般ユーザー向けAPI）に公開される。

| 値 | ラベル | 説明 |
|---|---|---|
| `BEFORE_PARTICIPATION` | 参画前 | 登録済みだがまだ参画していない状態 |
| `PARTICIPATING` | 参画中 | 参画中。CS APIに公開される |
| `DROPPED` | 参画落ち | 参画から外れた終端状態。以降の変更不可 |

## Prefecture（Enum）

47都道府県を表すEnum。

`HOKKAIDO`, `AOMORI`, `IWATE`, `MIYAGI`, `AKITA`, `YAMAGATA`, `FUKUSHIMA`, `IBARAKI`, `TOCHIGI`, `GUNMA`, `SAITAMA`, `CHIBA`, `TOKYO`, `KANAGAWA`, `NIIGATA`, `TOYAMA`, `ISHIKAWA`, `FUKUI`, `YAMANASHI`, `NAGANO`, `SHIZUOKA`, `AICHI`, `MIE`, `SHIGA`, `KYOTO`, `OSAKA`, `HYOGO`, `NARA`, `WAKAYAMA`, `TOTTORI`, `SHIMANE`, `OKAYAMA`, `HIROSHIMA`, `YAMAGUCHI`, `TOKUSHIMA`, `KAGAWA`, `EHIME`, `KOCHI`, `FUKUOKA`, `SAGA`, `NAGASAKI`, `KUMAMOTO`, `OITA`, `MIYAZAKI`, `KAGOSHIMA`, `OKINAWA`

## ShopImage（エンティティ）

### プロパティ

| プロパティ | 型 | 説明 |
|-----------|---|------|
| id | ShopImageId | 画像ID |
| type | ShopImageType | 画像の種別（main, logo） |
| imageUrl | ImageUrl | 画像URL |

## 関連する集約

| 集約 | 関連 | 説明 |
|------|------|------|
| [CoffeeBean](./coffeeBean.md) | 被参照 | 珈琲豆がこの店舗を参照する |
