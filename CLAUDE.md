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
