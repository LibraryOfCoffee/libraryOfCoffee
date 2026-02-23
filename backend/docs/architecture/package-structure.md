# パッケージ構成規約

## マルチモジュール構成

```
backend/
├── common/       # 共有コード：ドメインモデル、リポジトリIF、インフラ実装
├── cs-api/       # 顧客向けAPI (port 8080)
└── admin-api/    # 管理者向けAPI (port 8081)
```

`cs-api`と`admin-api`は`common`モジュールに依存する。ドメインロジック・永続化層はすべて`common`に配置する。

## common モジュール (`com.mametosho`)

```
common/src/main/kotlin/com/mametosho/
├── domain/
│   ├── model/
│   │   ├── {aggregate}/          # 集約ごとにパッケージを分ける
│   │   └── shared/               # 複数集約で共用する値オブジェクト
│   └── repository/               # リポジトリIF（コマンド側）
└── infrastructure/
    ├── persistence/
    │   ├── mybatis/
    │   │   ├── mapper/            # MyBatisマッパー（コマンド側）
    │   │   └── entity/            # DBエンティティ
    │   └── repository/            # リポジトリ実装
    └── config/                    # SecurityConfigなど
```

## cs-api / admin-api モジュール (`com.mametosho.cs` / `com.mametosho.admin`)

```
{module}/src/main/kotlin/com/mametosho/{cs|admin}/
├── application/
│   ├── usecase/                       # ユースケース（コマンド側、@Service）
│   └── query/                         # クエリサービス（CQRS読み取り側）
│       ├── {Feature}QueryService.kt   # IF — query/ 直下に配置
│       └── result/                    # Result classes — result/ 配下に配置
│           ├── {Feature}DetailResult.kt
│           └── ...
├── infrastructure/
│   └── persistence/
│       ├── mybatis/
│       │   ├── entity/                # DBエンティティ（Row data class含む）
│       │   │   └── {Feature}DetailRow.kt
│       │   └── mapper/                # MyBatisマッパー
│       │       └── {Feature}QueryMapper.kt
│       └── repository/                # リポジトリ実装（QueryServiceImpl含む）
│           └── {Feature}QueryServiceImpl.kt
├── presentation/
│   ├── controller/                    # RESTコントローラ
│   └── dto/
│       └── response/                  # レスポンスDTO
└── config/                            # OpenApiConfigなど
```

### レイヤー依存ルール

ArchUnitテスト（`LayerDependencyTest`）で自動検証される。

- **infrastructure層** は presentation層 に依存しない
- **application層** は infrastructure層・presentation層 に依存しない
- infrastructure → application の依存は許可（QueryServiceImpl が QueryService IF を実装するため）

### パッケージ構成のポイント

- QueryService IFは `application/query/` **直下**に配置する（サブパッケージを切らない）
- Result classesは `application/query/result/` 配下に配置する
- Mapper は `infrastructure/persistence/mybatis/mapper/` に配置
- Row data class（entity）は `infrastructure/persistence/mybatis/entity/` に配置
- QueryServiceImpl は `infrastructure/persistence/repository/` に配置

### コーディングルール

- **non-null assertion (`!!`) は使わない。** `checkNotNull` を使い、エラーメッセージを付与する
