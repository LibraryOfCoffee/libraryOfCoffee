---
allowed-tools: Bash
description: "CS APIをローカル起動する (port 8080)"
---

# start-cs-api

CS APIをローカルプロファイルで起動する。

## 手順

1. ポート8080が既に使用されている場合は、そのプロセスをkillする:

```shell
lsof -ti :8080 | xargs kill 2>/dev/null || true
```

2. backendディレクトリで以下のコマンドを実行する:

```shell
cd backend && SPRING_PROFILES_ACTIVE=local ./gradlew :cs-api:bootRun
```

3. 起動が完了したら、ユーザーに `http://localhost:8080` でアクセス可能であることを伝える
