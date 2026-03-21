---
allowed-tools: Bash(pnpm:*), Bash(cd:*)
description: "OpenAPIスペックからCS APIのTypeScript APIクライアント型定義を再生成する"
---

# generate-cs-api-client

OpenAPIスペック（docs/swagger/cs-api.yml）からTypeScript型定義を再生成するコマンド。

## 手順

1. `cd frontend && pnpm api:generate` を実行して型定義を再生成する
2. `cd frontend && pnpm lint` を実行して生成されたコードがlintを通ることを確認する
3. 生成結果をユーザーに報告する

## 使用タイミング

- バックエンドCS APIのエンドポイントやスキーマが変更された後
- `docs/swagger/cs-api.yml` が更新された後
- `./gradlew generateAllOpenApiDocs` を実行した後
