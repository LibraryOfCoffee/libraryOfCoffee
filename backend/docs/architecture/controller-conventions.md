# コントローラ実装規約

## 基本構造

```kotlin
@RestController
@RequestMapping("/api/{resource}")
@Tag(name = "ResourceName", description = "リソースAPI")
class ResourceController(
    private val queryService: ResourceQueryService,  // or usecase
) {
    // ...
}
```

## OpenAPIアノテーション

### 必須アノテーション

- `@Tag` — クラスレベルでAPIグループを定義
- `@Operation` — `summary` と `description` を **両方** 記載
- `@ApiResponses` — 各ステータスコードのレスポンスを定義
- `@Parameter` — パスパラメータの説明。`example` を含める

### ExampleObject

**成功・エラーの両方** に `@ExampleObject` で実際のJSONレスポンス例を含める。

- 200応答: `schema` + `examples` を `Content` に指定
- エラー応答（404等）: `examples` のみ `Content` に指定。Spring Bootデフォルトのエラー形式（`timestamp`, `status`, `error`, `path`）で記述する

### ExampleObjectのインデント規約

- raw string (`"""..."""`) の中身は **Kotlinコードのインデントレベルに揃える**
- JSON自体のネストはスペース2つでインデントする
- 1行JSON（`"""{"id": 1}"""`）は **使わない**。必ず複数行で記述する

## 標準実装リファレンス

以下は新しいコントローラを実装する際のテンプレートとなる完全なコード例。

```kotlin
@RestController
@RequestMapping("/api/samples")
@Tag(name = "Sample", description = "サンプルAPI")
class SampleController(
    private val findSampleDetailUsecase: FindSampleDetailUsecase,
) {
    @GetMapping("/{id}")
    @Operation(
        summary = "サンプル取得",
        description = "指定されたIDのサンプルを取得します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "取得成功",
                content = [
                    Content(
                        schema = Schema(implementation = SampleResponse::class),
                        examples = [
                            ExampleObject(
                                name = "success",
                                summary = "取得成功例",
                                value = """
                                    {
                                      "id": 1,
                                      "name": "コーヒー豆A"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "サンプルが見つかりません",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                name = "not_found",
                                summary = "サンプルが見つからない場合",
                                value = """
                                    {
                                      "timestamp": "2026-02-23T12:00:00.000+00:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "path": "/api/samples/999"
                                    }
                                """,
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getSampleById(
        @Parameter(description = "サンプルID", required = true, example = "1")
        @PathVariable id: Long,
    ): ResponseEntity<SampleResponse> {
        val sample = findSampleDetailUsecase.execute(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SampleResponse.from(sample))
    }
}
```

### チェックリスト

新しいコントローラを実装するときに確認すること:

- [ ] `@Tag` でAPIグループを定義しているか
- [ ] `@Operation` に `summary` と `description` の両方があるか
- [ ] 200応答に `schema` と `examples`（`@ExampleObject`）があるか
- [ ] エラー応答（404等）にも `examples`（`@ExampleObject`）があるか
- [ ] エラーのexampleはSpring Bootデフォルト形式（`timestamp`, `status`, `error`, `path`）か
- [ ] `@Parameter` に `example` があるか
- [ ] ExampleObjectのJSONは複数行でインデントが揃っているか
- [ ] 末尾カンマのスタイルが統一されているか

## レスポンスDTO

### Schemaアノテーション

- クラスレベル: `@Schema(description = "...")`
- 各プロパティ: `@Schema(description = "...", example = "...")`
- nullableなフィールド: `@Schema(description = "...", example = "...", nullable = true)`

### ネスト構造

ネストが深い場合はレスポンスDTOクラス内にネストクラスとして定義する。

```kotlin
@Schema(description = "グループ詳細レスポンス")
data class GroupDetailResponse(
    val id: String,
    val items: List<ItemDetail>,
) {
    @Schema(description = "アイテム詳細")
    data class ItemDetail(
        val id: String,
        val name: String,
    ) {
        companion object {
            fun from(result: ItemDetailResult): ItemDetail { ... }
        }
    }

    companion object {
        fun from(result: GroupDetailResult): GroupDetailResponse { ... }
    }
}
```

### fromメソッド

Result → Response DTOへの変換は `companion object` の `from()` メソッドで行う。
