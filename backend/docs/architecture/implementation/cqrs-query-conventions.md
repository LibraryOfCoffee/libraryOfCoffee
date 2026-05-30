# CQRSクエリ側 実装規約

## 概要

読み取り専用のAPIエンドポイントでは、CQRS（コマンド・クエリ責務分離）のクエリ側パターンを使用する。
クエリ側はドメイン集約を経由せず、DB → Result に直接マッピングする。

## ドメインモデルを直接使う場合（Queryモデル分離不要）

**単純なCRUDで、クエリ結果がドメインモデルと一致する場合はQueryモデルを分離しない。**
リポジトリ経由でドメインモデルを返す方法を取る。

```
Controller → Usecase → Repository(IF) → RepositoryImpl → Mapper → DB
                                               ↓ ドメインモデルに変換
                                           Aggregate → Usecase → Controller → Response DTO
```

### 判断基準

| 条件 | 方針 |
|------|------|
| APIレスポンスに必要なデータがドメインモデルで表現できる | **リポジトリ経由（Queryモデル不要）** |
| APIレスポンスとドメインモデルが乖離している（集計・変換・JOIN結合が必要など） | QueryService + QueryServiceImpl |

「ドメインモデルで取得できるから経由する」ではなく「ユースケースに最適な取得方法を選ぶ」が基準。
シンプルなケースにQueryモデルを強制するのはオーバーエンジニアリングになる。

## 実装フロー

```
Controller → Usecase → QueryService IF → QueryServiceImpl → Mapper → DB
                                              ↓ Kotlinでグルーピング
                                           Result classes → Usecase → Controller → Response DTO
```

- **Controller は Usecase を経由する。** QueryService を直接呼ばない
- 実装順序: controller + usecase → queryService(IF) + result classes → queryServiceImpl + mapper + row

## QueryService IF

`application/query/` 直下に配置する。

```kotlin
package com.mametosho.admin.application.query

interface CoffeeBeanQueryService {
    fun findDetail(id: String): CoffeeBeanDetailResult?
}
```

## Result classes

`application/query/result/` 配下に配置する。プリミティブ型（String, Int等）で構成する。

```kotlin
package com.mametosho.admin.application.query.result

data class CoffeeBeanDetailResult(
    val id: String,
    val shopId: String,
    val name: String,
    val description: String,
    val images: List<ImageResult>,
    val tastes: List<TasteResult>,
) {
    data class ImageResult(val id: String, val type: String, val imageUrl: String)
    data class TasteResult(val id: String, val tasteName: String, val evaluationValue: Int)
}
```

## MyBatis Mapper

Mapper は `infrastructure/persistence/mybatis/mapper/` に配置する。

**フラットな行データ（Row data class）を返す。** MyBatisの `@Many(resultMap, columnPrefix)` による階層マッピングは使わない。

```kotlin
@Mapper
interface CoffeeBeanQueryMapper {

    @Select("""
        SELECT cb.id AS bean_id, cb.shop_id, cb.name AS bean_name, cb.description,
               cbi.id AS image_id, cbi.type AS image_type, cbi.image_url,
               cbt.id AS taste_eval_id, t.name AS taste_name, cbt.evaluation_value
        FROM coffee_beans cb
        LEFT JOIN coffee_bean_images cbi ON cbi.coffee_bean_id = cb.id
        LEFT JOIN coffee_bean_tastes cbt ON cbt.coffee_bean_id = cb.id
        LEFT JOIN tastes t ON cbt.tastes_id = t.id
        WHERE cb.id = #{id}
    """)
    fun findDetailRowsById(id: String): List<CoffeeBeanDetailRow>
}
```

### JOINの使い分け

- **INNER JOIN** — FK制約+NOT NULLで存在が保証されるテーブル（例: `tastes` をFK先として取得する場合など）
- **LEFT JOIN** — 0件の可能性があるテーブル（例: `coffee_bean_images`, `coffee_bean_tastes`）

### Row data class（= entity）

`infrastructure/persistence/mybatis/entity/` に配置する。JOINクエリの1行に対応するフラットなデータクラス。

**nullabilityはSQLのJOIN種別に合わせる。** INNER JOINで必ず取れるカラムはnon-null、LEFT JOINで欠損しうるカラムはnullable。

```kotlin
package com.mametosho.infrastructure.persistence.mybatis.entity

data class CoffeeBeanDetailRow(
    val beanId: String,         // 主テーブル → non-null
    val shopId: String,
    val beanName: String,
    val description: String,
    val imageId: String?,       // LEFT JOIN → nullable
    val imageType: String?,
    val imageUrl: String?,
    val tasteEvalId: String?,   // LEFT JOIN → nullable
    val tasteName: String?,
    val evaluationValue: Int?,
)
```

## QueryServiceImpl — Kotlinでグルーピング

`infrastructure/persistence/query/` に配置する。
Mapperから返されたフラットな行を、Kotlinの `filter` / `distinctBy` で階層構造に組み立てる。

```kotlin
@Service
class CoffeeBeanQueryServiceImpl(
    private val mapper: CoffeeBeanQueryMapper,
) : CoffeeBeanQueryService {

    override fun findDetail(id: String): CoffeeBeanDetailResult? {
        val rows = mapper.findDetailRowsById(id)
        if (rows.isEmpty()) return null

        val first = rows.first()

        return CoffeeBeanDetailResult(
            id = first.beanId,      // 主テーブル → non-null、そのまま使える
            shopId = first.shopId,
            name = first.beanName,
            description = first.description,
            images = rows           // LEFT JOIN → nullable、filterしてcheckNotNull
                .filter { it.imageId != null }
                .distinctBy { it.imageId }
                .map { row ->
                    CoffeeBeanDetailResult.ImageResult(
                        id = checkNotNull(row.imageId) { "imageId must not be null" },
                        type = checkNotNull(row.imageType) { "imageType must not be null" },
                        imageUrl = checkNotNull(row.imageUrl) { "imageUrl must not be null" },
                    )
                },
            tastes = rows
                .filter { it.tasteEvalId != null }
                .distinctBy { it.tasteEvalId }
                .map { row ->
                    CoffeeBeanDetailResult.TasteResult(
                        id = checkNotNull(row.tasteEvalId) { "tasteEvalId must not be null" },
                        tasteName = checkNotNull(row.tasteName) { "tasteName must not be null" },
                        evaluationValue = checkNotNull(row.evaluationValue) { "evaluationValue must not be null" },
                    )
                },
        )
    }
}
```

### なぜMyBatisでグルーピングしないのか

- MyBatisの `@Many` + `columnPrefix` はダミーメソッドホルダーが必要で見通しが悪い
- Kotlinのコレクション操作は可読性が高く、テストも容易
- フラットなRowはデバッグが容易
