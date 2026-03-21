---
allowed-tools: Bash
description: "CS APIをローカル起動する (port 8080)"
---

# start-cs-api

CS APIをローカルプロファイルで起動する。

## 手順

1. backendディレクトリで以下のコマンドを実行する:

```shell
cd /Users/satoyuki/Program/libraryOfCoffee-worktree/libraryOfCoffee/backend && SPRING_PROFILES_ACTIVE=local ./gradlew :cs-api:bootRun
```

2. 起動が完了したら、ユーザーに `http://localhost:8080` でアクセス可能であることを伝える
