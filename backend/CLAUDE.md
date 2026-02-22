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
```

## 技術スタック

- **Kotlin** 2.3.0 / **Java** 25
- **Spring Boot** 4.0.0
- **MyBatis** 4.0.0（ORMではなくSQLマッパー）
- **MySQL** 8.4（localプロファイルではDocker Composeで自動起動）
- **springdoc-openapi** 2.8.6（Swagger UI）

## アーキテクチャ

### マルチモジュール構成

```
backend/
├── common/       # 共有コード：ドメインモデル、リポジトリIF、インフラ実装
├── cs-api/       # 顧客向けAPI (port 8080)
└── admin-api/    # 管理者向けAPI (port 8081)
```

`cs-api`と`admin-api`は`common`モジュールに依存する。ドメインロジック・永続化層はすべて`common`に配置する。

### DDDレイヤー構造

各モジュール内のパッケージ構成はDDD（ドメイン駆動設計）に従う。

**common モジュール** (`com.mametosho`):
- `domain.model/` — ドメインモデル（集約ルート、エンティティ、値オブジェクト）
- `domain.repository/` — リポジトリインターフェース
- `infrastructure.persistence.mapper/` — MyBatisマッパー（インターフェース）
- `infrastructure.persistence.entity/` — DBエンティティ
- `infrastructure.persistence.repository/` — リポジトリ実装
- `infrastructure.config/` — SecurityConfigなど

**cs-api / admin-api** (`com.mametosho.cs` / `com.mametosho.admin`):
- `application.usecase/` — ユースケース（`@Service`）
- `presentation.controller/` — RESTコントローラ
- `presentation.dto.response/` — レスポンスDTO
- `config/` — OpenApiConfigなど

### コード追加時の指針

- **新しいAPIエンドポイント**: controller → usecase → repository(IF) → repositoryImpl + mapper の順で実装
- **ドメインモデル**: `common/domain/model/` に配置。Spring依存を持たせない。実装規約は [`docs/architecture/domain-model-conventions.md`](docs/architecture/domain-model-conventions.md) を参照
- **リポジトリ**: インターフェースは`domain/repository/`、実装は`infrastructure/persistence/repository/`
- **コントローラ**: Swagger/OpenAPIアノテーション（`@Operation`, `@ApiResponses`, `@Tag`等）を付与する

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
