# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 開発コマンド

```shell
# CS API 起動 (port 8080)
SPRING_PROFILES_ACTIVE=local ./gradlew :cs-api:bootRun

# Admin API 起動 (port 8081)
SPRING_PROFILES_ACTIVE=local ./gradlew :admin-api:bootRun

# ビルド
./gradlew build

# テスト実行
./gradlew test

# 単一モジュールのテスト
./gradlew :cs-api:test
./gradlew :admin-api:test
./gradlew :common:test

# 単一テストクラス実行
./gradlew :cs-api:test --tests "com.mametosho.cs.SomeTest"

# OpenAPIドキュメント生成（docs/swagger/に出力）
./gradlew generateAllOpenApiDocs

# 静的解析（detekt）
./gradlew detekt
```

## 技術スタック

- **Kotlin** 2.3.0 / **Java** 25
- **Spring Boot** 4.0.0
- **MyBatis** 4.0.0（ORMではなくSQLマッパー）
- **MySQL** 8.4（localプロファイルではDocker Composeで自動起動）
- **springdoc-openapi** 2.8.6（Swagger UI）
- **detekt** 1.23.8（Kotlin静的解析）

## アーキテクチャ・実装規約

設計観点ごとにドキュメントを分けている。実装時は該当するドキュメントを参照すること。

| 観点 | ドキュメント |
|------|-------------|
| パッケージ構成・モジュール構成 | [`docs/architecture/package-structure.md`](docs/architecture/package-structure.md) |
| ドメインモデル実装規約 | [`docs/architecture/domain-model-conventions.md`](docs/architecture/domain-model-conventions.md) |
| CQRSクエリ側実装規約 | [`docs/architecture/cqrs-query-conventions.md`](docs/architecture/cqrs-query-conventions.md) |
| コントローラ・OpenAPI実装規約 | [`docs/architecture/controller-conventions.md`](docs/architecture/controller-conventions.md) |

### コード追加時の指針

- **新しいAPIエンドポイント（コマンド側）**: controller → usecase → repository(IF) → repositoryImpl + mapper の順で実装
- **新しいAPIエンドポイント（クエリ側・CQRS）**: [CQRSクエリ側実装規約](docs/architecture/cqrs-query-conventions.md) に従う
- **ドメインモデル**: [ドメインモデル実装規約](docs/architecture/domain-model-conventions.md) に従う
- **コントローラ**: [コントローラ・OpenAPI実装規約](docs/architecture/controller-conventions.md) に従う

## プロファイル

| プロファイル | 用途 | DB | 備考 |
|------------|------|-----|------|
| `local` | ローカル開発 | MySQL (Docker Compose) | スキーマ・データ自動初期化、デバッグログ有効 |
| `dev` | 開発環境 | MySQL | DB初期化なし |
| `openapi` | OpenAPIドキュメント生成用 | H2 (インメモリ) | Docker Compose無効、Spring Security無効 |

## データベース

- スキーマ定義: `infrastructure/db/schema.sql`
- ローカルデータ: `infrastructure/db/local-data.sql`
- MyBatisは`map-underscore-to-camel-case: true`でsnake_case → camelCase自動変換

## ドメインモデルのドキュメント

`docs/domainModel.md` に集約一覧とドメインモデル図がある。各集約の詳細は `docs/domains/` 配下を参照。
