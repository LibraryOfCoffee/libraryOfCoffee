---
allowed-tools: Bash
description: "Admin APIをローカル起動する (port 8081)"
---

# start-admin-api

Admin APIをローカルプロファイルで起動する。

## 手順

1. backendディレクトリで以下のコマンドを実行する:

```shell
cd /Users/satoyuki/Program/libraryOfCoffee-worktree/libraryOfCoffee/backend && SPRING_PROFILES_ACTIVE=local ./gradlew :admin-api:bootRun
```

2. 起動が完了したら、ユーザーに `http://localhost:8081` でアクセス可能であることを伝える
