# Shop 集約

## 概要

珈琲豆を提供する店舗を表す集約。
Shopifyのショップと1対1で紐づく。

## ライフサイクル

- **登録**: 管理者が管理画面から登録する。初期状態は任意に選択でき、デフォルトは下書き（draft）
- **更新**: 店舗情報・画像の変更
- **公開状態の切り替え**: 管理者が draft ⇄ published を双方向に切り替えられる。draft の店舗はCS（一般ユーザー向け）には公開されない

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
| publishStatus | PublishStatus | 公開状態（draft / published） |
| images | List\<ShopImage\> | 店舗画像一覧 |

### 不変条件

- ShopifyShopIdはシステム内で一意でなければならない
- nameは必須（255文字以内）
- introductionは非null時、空白不可（10000文字以内）
- particularは非null時、空白不可（10000文字以内）
- shopUrlは必須、空白不可（2048文字以内）
- prefectureは必須
- LOGO画像はちょうど1枚（必須）

## PublishStatus（Enum）

公開状態を表すEnum。draft（下書き・非公開）と published（公開）の双方向の遷移が可能。
draft の店舗はCS（一般ユーザー向けAPI）には公開されない。

`DRAFT`, `PUBLISHED`

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
