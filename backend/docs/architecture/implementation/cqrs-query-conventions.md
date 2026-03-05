# CQRSクエリ側 実装規約

## 概要

読み取り専用のAPIエンドポイントでは、CQRS（コマンド・クエリ責務分離）のクエリ側パターンを使用する。
クエリ側はドメイン集約を経由せず、DB → Result に直接マッピングする。

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
package com.mametosho.cs.application.query

interface CoffeeListGroupQueryService {
    fun findDetailById(id: String): CoffeeListGroupDetailResult?
}
```

## Result classes

`application/query/result/` 配下に配置する。プリミティブ型（String, Int等）で構成する。

```kotlin
package com.mametosho.cs.application.query.result

data class CoffeeListGroupDetailResult(
    val id: String,
    val description: String?,
    val coffeeBeans: List<CoffeeBeanDetailResult>,
)
```

## MyBatis Mapper

Mapper は `infrastructure/persistence/mybatis/mapper/` に配置する。

**フラットな行データ（Row data class）を返す。** MyBatisの `@Many(resultMap, columnPrefix)` による階層マッピングは使わない。

```kotlin
@Mapper
interface CoffeeListGroupQueryMapper {

    @Select("SELECT ... FROM ... INNER JOIN ... LEFT JOIN ... WHERE ...")
    fun findDetailRowsById(id: String): List<CoffeeListGroupDetailRow>
}
```

### JOINの使い分け

- **INNER JOIN** — FK制約+NOT NULLで存在が保証されるテーブル（例: `coffee_beans`, `tastes`）
- **LEFT JOIN** — 0件の可能性があるテーブル（例: `coffee_bean_images`, `coffee_bean_tastes`）

### Row data class（= entity）

`infrastructure/persistence/mybatis/entity/` に配置する。JOINクエリの1行に対応するフラットなデータクラス。

**nullabilityはSQLのJOIN種別に合わせる。** INNER JOINで必ず取れるカラムはnon-null、LEFT JOINで欠損しうるカラムはnullable。

```kotlin
package com.mametosho.cs.infrastructure.persistence.mybatis.entity

data class CoffeeListGroupDetailRow(
    val groupId: String,
    val groupDescription: String?,
    val beanId: String,           // INNER JOIN → non-null
    val beanName: String,         // INNER JOIN → non-null
    // ...
    val imageId: String?,         // LEFT JOIN → nullable
    val imageType: String?,       // LEFT JOIN → nullable
    // ...
)
```

## QueryServiceImpl — Kotlinでグルーピング

`infrastructure/persistence/repository/` に配置する。
Mapperから返されたフラットな行を、Kotlinの `groupBy` / `distinctBy` で階層構造に組み立てる。

```kotlin
@Service
class CoffeeListGroupQueryServiceImpl(
    private val mapper: CoffeeListGroupQueryMapper,
) : CoffeeListGroupQueryService {

    override fun findDetailById(id: String): CoffeeListGroupDetailResult? {
        val rows = mapper.findDetailRowsById(id)
        if (rows.isEmpty()) return null

        val firstRow = rows.first()

        val coffeeBeans = rows
            .groupBy { it.beanId }
            .map { (_, beanRows) ->
                val bean = beanRows.first()
                CoffeeBeanDetailResult(
                    id = bean.beanId,      // INNER JOIN → non-null、そのまま使える
                    // ...
                    images = beanRows      // LEFT JOIN → nullable、filterしてcheckNotNull
                        .filter { it.imageId != null }
                        .distinctBy { it.imageId }
                        .map { row -> CoffeeBeanImageDetailResult(
                            id = checkNotNull(row.imageId) { "imageId must not be null" },
                            // ...
                        ) },
                )
            }

        return CoffeeListGroupDetailResult(
            id = firstRow.groupId,
            description = firstRow.groupDescription,
            coffeeBeans = coffeeBeans,
        )
    }
}
```

### なぜMyBatisでグルーピングしないのか

- MyBatisの `@Many` + `columnPrefix` はダミーメソッドホルダーが必要で見通しが悪い
- Kotlinのコレクション操作は可読性が高く、テストも容易
- フラットなRowはデバッグが容易
