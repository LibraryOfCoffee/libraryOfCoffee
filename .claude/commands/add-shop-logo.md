---
description: LPトップの参加店舗セクションに新しい店舗ロゴを追加する
---

# 店舗ロゴ追加

ユーザーから以下の情報を受け取って、LPの参加店舗セクションにロゴを追加してください。

## 必要な情報

- ロゴ画像（ユーザーが貼り付けるか、ファイルパスを指定）
- 店舗のWebサイトURL

## 手順

1. ロゴ画像を `frontend/public/shopLogos/` にコピーする
   - ファイル名はキャメルケース（例: `fiveCoffee.png`）
   - 店舗名から適切な名前を付ける
2. `frontend/src/app/lp/_components/PartnerShops/partnerShops.tsx` の `shops` 配列に新しいエントリを追加する
   - `name`: 店舗の表示名
   - `logoUrl`: `/shopLogos/<ファイル名>`
   - `websiteUrl`: 店舗のWebサイトURL

## 引数

$ARGUMENTS
