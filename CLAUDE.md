## セットアップ
コード開発を始める前に、以下のコマンドを実行してください。
```shell
mise trust
mise install
```

## frontend
フロントエンドのコードを変更した後は、必ずbiomeのチェックを実行してください。
```shell
cd frontend && pnpm lint
```

## backend
### localでの起動コマンド
```:shell
# CS API
SPRING_PROFILES_ACTIVE=local ./gradlew :cs-api:bootRun
```
```:shell
# Admin API
SPRING_PROFILES_ACTIVE=local ./gradlew :admin-api:bootRun
```
```:shell
./gradlew generateAllOpenApiDocs
```
