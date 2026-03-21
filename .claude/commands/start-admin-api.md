---
allowed-tools: Bash
description: "Admin APIをローカル起動する (port 8081)"
---

# start-admin-api

Admin APIをローカルプロファイルで起動する。

## 手順

1. ポート8081が既に使用されている場合は、そのプロセスをkillする:

```shell
lsof -ti :8081 | xargs kill 2>/dev/null || true
```

2. backendディレクトリで以下のコマンドを実行する:

```shell
cd /Users/satoyuki/Program/libraryOfCoffee-worktree/libraryOfCoffee/backend && SPRING_PROFILES_ACTIVE=local ./gradlew :admin-api:bootRun
```

3. 起動が完了したら、ユーザーに `http://localhost:8081` でアクセス可能であることを伝える
